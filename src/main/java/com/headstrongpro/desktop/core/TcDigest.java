package com.headstrongpro.desktop.core;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.30.
 */
public class TcDigest {

    private SystemChange systemChange;

    private static final String seed = "kljhH%945H5S3kj9s65n";

    /***
     * Default constructor
     */
    public TcDigest() {
        systemChange = new SystemChange();
    }

    /***
     * Default encoding method. Outputs processed string with UTF-8 representation
     * @param input string of text
     * @return digested string of text
     */
    public String encode(String input){
        return processParallel(input, seed, ActionType.ENCODE, DigestSystem.UTF8, BufferSize.SMALL);
    }

    /***
     * decoding method made for test purposes
     * @param input string of text
     * @return decrypted value
     */
    private String decode(String input){
        return processParallel(input, seed, ActionType.DECODE, DigestSystem.UTF8, BufferSize.SMALL);
    }

    /***
     * Custom encoding method. Outputs processed string of text with specified parameters
     * @param input string of text
     * @param seed String of text used to compute data
     * @param system output text representation: [UTF8 / BIN / HEX]
     * @return digested string of text
     */
    public String encode(String input, String seed, DigestSystem system){
        return processParallel(input, seed, ActionType.ENCODE, system, BufferSize.STANDARD);
    }

    /***
     * Custom decoding method. Outputs processed string of text with specified parameters
     * @param input string of text
     * @param seed String of text used to compute data
     * @param system output text representation: [UTF8 / BIN / HEX]
     * @return decrypted string of text
     */
    public String decode(String input, String seed, DigestSystem system){
        return processParallel(input, seed, ActionType.DECODE, system, BufferSize.STANDARD);
    }

    /***
     * Computes the data with the given hash code parallelly
     * @param input String of text / data to be processed
     * @param seed A string seed that is used to process the data
     * @param flag Action type: [ENCODE / DECODE]
     * @param system Numerical system of input / output: [UTF8 / BIN / HEX]
     * @param bufferSize buffer size
     * @return String of digested data
     */
    private String processParallel(String input, String seed, ActionType flag, DigestSystem system, BufferSize bufferSize) {
        ArrayList<String> buffer = partition(input, bufferSize); //partitions the long strings to enable concurrent data processing
        return buffer.parallelStream()
                .map(e -> {
                    try {
                        return e = run(e, seed, flag, system);
                    } catch (DigestRuntimeException e1) {
                        e1.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.joining());
    }

    private String run(String input, String seed, ActionType flag, DigestSystem system) throws DigestRuntimeException {
        int digestHash = 0;
        for (int temp : seed.toCharArray()){
            digestHash += temp; //Converts string seed to a numeric value
        }
        if(system.equals(DigestSystem.UTF8)) digestHash = digestHash % 2048; //if the output is represented as a UTF-8 string, then it uses only uses ASCII chars of values 0-2048
        StringBuilder output = new StringBuilder();
        if(flag.equals(ActionType.ENCODE)){ //for encoding
            for (int character : input.toCharArray()){ //iterates through each character in an input string
                if(system.equals(DigestSystem.UTF8)){
                    output.append(Character.toString((char) (character + digestHash))); //computes the output value
                } else {
                    /*if(system.equals(DigestSystem.BIN)){
                        output.append(systemChange.toNumericSystem(character + digestHash, 2)); //computes the output value
                    }*/ //doesn't work
                    if(system.equals(DigestSystem.HEX)){
                        output.append(systemChange.toNumericSystem(character + digestHash, 16)); //computes the output value
                    }
                }
            }//end of loop
        }//end of encoding
        if(flag.equals(ActionType.DECODE)){ //for decoding; analogical to encoding but reversed
            if(system.equals(DigestSystem.UTF8)){
                for (int character : input.toCharArray()){
                    output.append((char) (character - digestHash));
                }
            } else {
                String[] tmpInput = input.split(" "); //if the encrypted input was represented as a base 16 or 2 values, then it splits the input to individual values
                for(String temp : tmpInput){
                    /*if(system.equals(DigestSystem.BIN)){
                        try{
                            output.append((char) (Integer.parseInt(systemChange.toDecimal(temp, 2)) - digestHash)); //computes the original character by it's encrypted value
                        } catch (NumberFormatException ex){
                            throw new DigestRuntimeException("Error processing data. Invalid String to integer conversion. Could not convert BIN encrypted data into a String.\n" + ex.getMessage(),
                                    ex.getCause());
                        }
                    }*/ //doesn't work
                    if(system.equals(DigestSystem.HEX)){
                        try{
                            output.append((char) (Integer.parseInt(systemChange.toDecimal(temp, 16)) - digestHash)); //computes the original character by it's encrypted value
                        } catch (NumberFormatException ex){
                            throw new DigestRuntimeException("Error processing data. Invalid String to integer conversion. Could not convert HEX encrypted data into a String.\n" + ex.getMessage(),
                                    ex.getCause());
                        }
                    }
                }
            }
        }//end of decoding
        if(output.length() == 0) throw new DigestRuntimeException("Unknown TcDigest exception!");
        return output.toString();
    }//end of run()

    /***
     *P artitions the input for a fixed sized blocks for parellel processing of the data
     * @param string Input sequence
     * @param buffer size of a buffer for data
     * @return output array
     */
    protected ArrayList<String> partition(String string, BufferSize buffer) {
        int buff = buffer.getValue();
        ArrayList<String> chars = new ArrayList<>();
        int blockCount = 1;
        if (string.length() <= buff) {
            chars.add(string);
        } else {
            while ((blockCount * buff) < string.length()) {
                chars.add(string.substring((blockCount - 1) * buff, blockCount * buff));
                blockCount++;
            }
            chars.add(string.substring((blockCount - 1) * buff, string.length()));
        }
        return chars;
    }

    private class SystemChange {
        private char decToHexChars(int digit) {
            if (digit == 10) return 'A';
            else if (digit == 11) return 'B';
            else if (digit == 12) return 'C';
            else if (digit == 13) return 'D';
            else if (digit == 14) return 'E';
            else if (digit == 15) return 'F';
            else return ' ';
        }

        private int hexCharsToDec(char tmp) {
            if (tmp == 'A') return 10;
            else if (tmp == 'B') return 11;
            else if (tmp == 'C') return 12;
            else if (tmp == 'D') return 13;
            else if (tmp == 'E') return 14;
            else if (tmp == 'F') return 15;
            else return tmp - 48;
        }

        String toNumericSystem(int numberToChange, int system) {
            StringBuilder ret = new StringBuilder();
            while (numberToChange != 0) {
                int tmp = numberToChange % system;
                if (tmp > 9) ret.insert(0, decToHexChars(tmp));
                else ret.insert(0, tmp);
                numberToChange /= system;
            }
            ret.append(" ");
            return ret.toString();
        }

        String toDecimal(String text, int system) {
            int ret = 0;
            int i = text.length() - 1;
            if (system == 16) {
                for (char b : text.toCharArray()) {
                    ret += hexCharsToDec(b) * Math.pow(system, i);
                    i--;
                }
            } else {
                for (int a : text.toCharArray()) {
                    ret += (a - 48) * Math.pow(system, i);
                    i--;
                }
            }
            return String.valueOf(ret);
        }
    }

    public enum ActionType {
        ENCODE,
        DECODE
    }

    public enum DigestSystem {
        UTF8,
        //BIN, doesn't work for some reason
        HEX
    }

    public enum BufferSize {
        TINY(8),
        SMALL(16),
        STANDARD(64),
        BIG(256),
        LARGE(512),
        OMGWTFBBQ(2048);

        private int bufferSize;

        BufferSize(int size) {
            bufferSize = size;
        }

        public int getValue() {
            return bufferSize;
        }
    }

    public class DigestRuntimeException extends Exception {

        DigestRuntimeException(String message) {
            super(message);
        }

        DigestRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
