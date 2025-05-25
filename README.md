# File Utility Plus v1.1

File Utility Plus v1.0 has is a very useful tool. 
To run it simply download the "FileUtilityPlus.zip" zip file and extract and run (this works in Windows only). No setup is required for this.

![image](https://github.com/user-attachments/assets/30603c9f-2d14-4989-a7cd-9c603ed4cfcb)

This program has four options:

1) CONVERT TO FILE: You can convert a folder and all of its contents into a single file.First select the folder; then a list will show you all the file names of that folder;click OK and then select the output file.
Note: The folder size should not be more than 3.9 GB( Giga Byte ) long in a FAT or FAT32 file system. If it is more than that you have to split the folder contents into two or more folders. This is because some file system does not support creating a large file ( e.g. 5GB etc.). You can use NTFS to create a large file greater than 4GB.

2) CONVERT TO FOLDER: This is reverse operation of 'Convert to file'. Just select the source file first and then select a destination folder.

3) SPLIT: You can split a file into two or more files. This is useful when you consider saving file in a backup device which can contain data less than the size of that file. To do this, first select the source file; then type the size of a destination file and then its name; repeat the operation until all source file data is written. When splitting always remember the splitting sequence.

4) COMBINE: This is reverse operation of 'Split'. To combine some files, first select the destination file and then repeatedly add some files to the destination file; a confirmation will let you stop your operation.  When combining always remember the previous splitting sequence.


An example:

Suppose that you have a folder named "Data" and its size is about 2 GB long. You want to CD-write the files but a CD can contain about 650 MB data.

In this case you have to convert the folder and all of its contents into a single file using "To File" button of this software. We assume that You made a file named "All.data" which will be the same size of the Data folder( 2GB ).

Now Split the file "All.data" into "Part1.data", "Part2.data", "Part3.data", "Part4.data" files.
To create these four files use the length ( 649 X 1024 X 1024 ) bytes  for all the four files.

Now You can write these four files into 4 CDs.

You can retrieve all the data from the 4 CDs in 2 ways.

(1)

   (a) By combining the four files into a single file named "All.data"( using "Combine" button ). In this case you have to combine the files sequentially. That is "Part1.data", then "Part2.data" and so on. 

   (b) Then you can use "To Folder" button of this software to retrieve all data from the file "All.data".


(2)The second way has only one step. Use the "To Folder" button and select the file "Part1.data" as source. After a while a "Select Next File" prompt will allow you to select the second file ( "Part2.data" ). This way after you have selected all the four data files sequentially the operation completes. This is a shortcut way which will be very useful to you.


By the way, you can exclude any computer virus files from the archive files when it is archiving( only if you know the file name; e.g. "Desktop.ini", "WinZipTmp.exe", "Folder.htt", "Temp.htt" etc. ). To search any file named "Desktop.ini" you have to type "*\desktop.ini" in the search keyword. You can exclude any viruses by searching.
