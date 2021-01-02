This repository has 1 java file named hackercamp.java

The program has two main funcitons;
1. Find ten biggest files on the system PC ::  void updatefilelist( Path file){}
2. Cleaning up the Desktop :: void desktopcleaner(Path file) {}

In the first function the "Files.walkFileTree" recursively iterates over all the disks and folders
the function prints the filespath of ten biggest files in decreasing order of file size along with their size.

Desktopcleaner() moves all the files except shortcuts and Folder on desktop to specific folders in
Document folder. The specific folders are named as the extension of the files. 

eg: abc.mp3 is moved to "Documents/mp3/abc.mp3"


