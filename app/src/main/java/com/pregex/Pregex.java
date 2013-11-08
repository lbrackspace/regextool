package com.pregex;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Pregex {
    private static String iregex = "";
    private static String imstr = null;
    private static String type = "";
    private static boolean vcmd = false;

    public static void main(String[] args) throws IOException {

        for (String arg : args) {
            if (arg.equalsIgnoreCase("help")) {
                prntHelp();
            } else if (arg.split("=")[0].equalsIgnoreCase("type")) {
                type = arg.split("=")[1];
            } else if (arg.split("=")[0].equalsIgnoreCase("regex")) {
                iregex = arg.split("=")[1];
            } else if (arg.split("=")[0].equalsIgnoreCase("mstr")) {
                imstr = arg.split("=")[1];
            } else {
                vcmd = true;
            }
        }

        if (vcmd || args == null || args.length == 0) {
            interactive();
        } else {
            validate();
        }
    }

    private static void prntHelp() {
        System.out.println("Validates regex against Java, Perl and PCRE libraries. \n\n" +
                "help: Returns this help menu. \n\n" +
                "Example usage: <regex=^*$> <mstr=200> \n\n" +
                "type: Use 'j' or 'p' for Java or Perl regex validation or 'pcre' for PCRE validation. Defaults to Perl validation \n" +
                "regex: The regex to test with. \n" +
                "mstr: The string to match against. \n\n" +
                "You can also run this program in interactive mode by not supplying any arguments. \n" +
                "Interactive usage: $> <type> <regex> <mstr> \n" +
                "Example: $> p ^*$ 200 :: type and regex are required. \n" +
                "While in interactive mode type 'help' to display this menu or 'exit' to quit. \n");
    }

    private static void interactive() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = null;
        try {
            System.out.println("Enter a regex to validate");
            while ((s = br.readLine()) != null) {
                if (s.equalsIgnoreCase("help")) {
                    prntHelp();
                } else if (s.equalsIgnoreCase("exit") || s.equals("")) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    try {
                        type = s.split(" ")[0];
                        iregex = s.split(" ")[1];
                    } catch (Exception aex) {
                        System.out.println("Please provide type and regex values as they are required. EX: $: p ^*$");
                    }
                    try {
                        imstr = s.split(" ")[2];
                    } catch (ArrayIndexOutOfBoundsException aex2) {
                        //ignore...
                    }
                    validate();
                }
            }
        } catch (IOException ioe) {
            System.out.println("Exception during interactive shell processing");
            System.out.println(ioe.getMessage());
        }
    }

    private static void validate() throws IOException {
        if (type.equalsIgnoreCase("pcre") && iregex != "") {
            vPcre();
        } else if (type.equalsIgnoreCase("j") && iregex != "") {
            vJava();
        } else if (iregex != "") {
            vPerl();
        }
    }

    private static void vJava() {
        try {
            Pattern re = Pattern.compile(iregex);
            System.out.println(re + " is a valid Java regex pattern!");
            if (imstr != null) {
                Matcher matcher = re.matcher(imstr);
                // check all occurance
                while (matcher.find()) {
                    System.out.println("Text: " + "'" + imstr + "'" + " matched for Java validator!");
                    System.out.println("Start index: " + matcher.start());
                    System.out.println("End index: " + matcher.end());
                    System.out.println(matcher.group());
                }
            }
        } catch (PatternSyntaxException exception) {
            System.err.println("Failed during Java regex validation: ");
            System.err.println(exception.getDescription());
        }
    }

    private static void vPerl() {
        //perl regex validation
        try {
            jregex.Pattern re = new jregex.Pattern(iregex);
            System.out.println(re + " is a valid Perl regex pattern!");
            if (imstr != null) {
                jregex.Matcher matcher = re.matcher(imstr);
                // check all occurance
                while (matcher.find()) {
                    System.out.println("Text: " + "'" + imstr + "'" + " matched for Perl validator!");
                    System.out.println("Start index: " + matcher.start());
                    System.out.println("End index: " + matcher.end());
                }
            }
        } catch (jregex.PatternSyntaxException exception) {
            System.err.println("Failed during Perl regex validation: ");
            System.err.println(exception.getMessage());
        }
    }

    private static void vPcre() {
        Process cPgm = null;
        try {
            cPgm = Runtime.getRuntime().exec("pcrevalidate");

            OutputStream stdin = cPgm.getOutputStream();
            InputStream rd = cPgm.getInputStream();
            stdin.write(iregex.getBytes());
            stdin.flush();
            stdin.close();
            InputStreamReader reader = new InputStreamReader(rd);
            Scanner scan = new Scanner(reader);
            String resp = scan.nextLine();
            scan.close();
            reader.close();
            rd.close();

            cPgm.destroy();

            String status = resp.split("\\|")[0];

            if (status.equals("BAD")) {
                String msg = resp.split("\\|")[1];
                System.out.println("Regex is invalid: " + msg);
            } else {
                System.out.println("Regex is valid: ");
            }
        } catch (IOException e) {
            System.out.println("PCRE Validation failed due to IOException: " + e.getMessage());
        }

    }
}
