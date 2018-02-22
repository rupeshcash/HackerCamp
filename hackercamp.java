import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import static java.nio.file.FileVisitResult.*;
import javax.swing.filechooser.FileSystemView;


public class hackercamp {

    public static class Finder extends SimpleFileVisitor<Path> {

        private int numMatches = 0;
        private int numfile = 0;
        private String[][] listfiles = new String[10][2];

        void printfiles() {
            for (int i = 9; i >=0; i--) {
                System.out.println(listfiles[i][0] + "  " + listfiles[i][1]);
            }
        }
        //method that moves file from desktop to specific folders in documents

        void desktopcleaner(Path file) {
            Path name = file.getFileName();
            //compares if the file is a shortcut
            PathMatcher matcherx = FileSystems.getDefault()
                    .getPathMatcher("glob:" + "*.lnk");

            if(name !=null && !matcherx.matches(name) && !file.toFile().isDirectory()) {
                File aFile = file.toFile();
                String filepath = aFile.getPath();
                //System.out.println(filepath);
                //finding extension from the filepath
                String namefolder="";
                if (filepath.length()>3 && aFile.isFile()) {
                    int i = filepath.lastIndexOf('.');
                    if (i > 0) {
                        namefolder = filepath.substring(i+1);
                    }
                    String str = (System.getProperty("user.home") + "/Documents/" + namefolder);
                    File directory = new File(str);
                    //check if the destination folder exists or not. If not then create a folder.
                    if (!directory.exists() || !directory.isDirectory()) {
                        directory.mkdirs();
                    }
                    try {
                        Files.move(Paths.get(aFile.getPath()), Paths.get(str + "/" + aFile.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   // aFile.renameTo(new File(str+ "/" + aFile.getName()));
                }
            }
    }

        //method to generate list of ten largest files in the pc
        void updatefilelist( Path file){
            Path name = file.getFileName();
            Long minimalfilesize = 0L;
            String filepath;

            if (name != null) {
                File aFile = file.toFile();
                filepath = aFile.getPath();


                    if (numfile <= 10) {
                        listfiles[numfile][0] = filepath;
                        listfiles[numfile][1] = String.valueOf(filepath.length());
                        numfile++;
                        if(numfile==10)
                        {
                            Arrays.sort(listfiles, new Comparator<String[]>() {
                                @Override
                                public int compare(final String[] entry1, final String[] entry2) {
                                    final Double time1 = Double.parseDouble(entry1[1]);
                                    final Double time2 = Double.parseDouble(entry2[1]);
                                    return time1.compareTo(time2);
                                }
                            });
                            numfile++;
                        }
                    }
                    else
                    {
                        if(Double.valueOf(listfiles[0][1]) < aFile.length()){
                            listfiles[0][0] = aFile.getPath();
                            listfiles[0][1] = String.valueOf(aFile.length());
                            Arrays.sort(listfiles, new Comparator<String[]>() {
                                @Override
                                public int compare(final String[] entry1, final String[] entry2) {
                                    final Double time1 = Double.parseDouble(entry1[1]);
                                    final Double time2 = Double.parseDouble(entry2[1]);
                                    return time1.compareTo(time2);
                                }
                            });
                             }
                    }
            }
        }

        //override functions to call the updatefilelist or desktopcleaner each time
        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attrs) {
          updatefilelist(file);
            return CONTINUE;
        }

        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                                                 BasicFileAttributes attrs) {
         updatefilelist(dir);
         return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            return CONTINUE;
        }
    }

    public static void main(String[] args)
            throws IOException {

        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        System.out.println("Choose");
        System.out.println("1. hackercamp ten bigest files");
        System.out.println("2. Clean Desktop");
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();

        if (a==1) {
            paths = File.listRoots();
            Finder finder = new Finder();
                for (File path : paths) {
                String str = path.toString();
                String slash = "\\";
                String s = new StringBuilder(str).append(slash).toString();
                Path startingDir = Paths.get(s);
                Files.walkFileTree(startingDir, finder);
            }

            finder.printfiles();
        }
        else if(a==2){
                Finder finder = new Finder();
                String str = (System.getProperty("user.home") + "/Desktop");
                String slash = "\\";
                String s = new StringBuilder(str).append(slash).toString();
                Path startingDir = Paths.get(s);
                File[] files = new File(s).listFiles();

                for(File path : files){
                    String strr = path.toString();
                    String slassh = "\\";
                    String ss = new StringBuilder(strr).toString();
                    Path ff = Paths.get(ss);
                    finder.desktopcleaner(ff);
                }
                System.out.println("DONE");
            }
        }
}
