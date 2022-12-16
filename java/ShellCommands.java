import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class test{

    /**
     * @param args
     */
    public static void main(String[] args){
        List<String> cmds = new ArrayList<>();
        // cmds.add("ls /home/ubuntuvm/Downloads/openJDK/jdk-17.0.5+8/lib/*.so");
        cmds.add("cat test.java | grep System.out.println");
        cmds.add("pwd");
        cmds.add("cd /home/ubuntuvm/");
        cmds.add("pwd");
        cmds.add("ls -ltr");
        cmds.add("ls /home/ubuntuvm/Downloads/openJDK/jdk-17.0.5+8/lib/*.so");
        try{
            // print(cmds);
            executeCommands(cmds).forEach(line -> System.out.println(line));;
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public static void print(String cmd){
        System.out.println(cmd+"\n");
        try {
            executeCommandOnce(cmd).forEach(line -> System.out.println(line));
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void print(List<String> cmd){
        cmd.forEach(i -> System.out.println(i));
        System.out.println("\n");
        try {
            executeCommands(cmd).forEach(line -> System.out.println(line));
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static List<String> executeCommandOnce(String cmd) throws Exception{
        List<String> output = new ArrayList<>();
        Process p;
        try{
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            output = reader.lines().map(l -> l.trim()).collect(Collectors.toList());
            p.waitFor();
        }catch(Exception exception){
            throw exception;
        }
        return output;
    }

    public static void executeCommand(String service) throws Exception{
        List<String> output = new ArrayList<>();
        Process p;
        try{
            //exec parser doesn't understand shell typos
            //better initiate a shell in the process and 
            //type commands as string array, /bin/sh -c 
            // initiates a shell
            String[] c = {"/bin/sh", "-c", service};
            p = Runtime.getRuntime().exec(c);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            output = reader.lines().map(l -> l.trim()).collect(Collectors.toList());
        }catch(Exception exception){
            throw exception;
        }
        output.forEach(i -> System.out.println(i));
    }

    public static List<String> executeCommands(List<String> commands) throws Exception{
        List<String> output = new ArrayList<>();
        ProcessBuilder builder;
        try {
            builder = new ProcessBuilder("/bin/bash");
            //outputstream closed error if builder inherits io
            // builder.inheritIO()
            builder.directory(new File(System.getProperty("user.home") + "/Documents/projects/"));
            Process p = builder.start();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            commands.forEach(cmd -> {
                try{
                    writer.write("echo");
                    writer.newLine();
                    writer.write("echo");
                    writer.newLine();
                    writer.write("echo \">>" + cmd + "\"");
                    writer.newLine();
                    writer.write("echo");
                    writer.newLine();
                    writer.write(cmd);
                    writer.newLine();
                    writer.flush();
                }catch(IOException e){
                    e.printStackTrace();
                }
            });
            //gets stuck if you don't exit
            try {
                writer.write("exit");
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            output = reader.lines().map(l -> l.trim()).collect(Collectors.toList());
            p.waitFor();
        } catch (Exception e) {
            throw e;
        }
        return output;
    }
}
