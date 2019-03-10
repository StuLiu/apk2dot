# apk2dot

#### Introduction
This project is constructed to transform apk file into dot file. So, we can get the call graph of an android apk file. And then, we can analysis the apk files with call graph easily.

#### 安装教程

1. android sdk with at least one version of sdk, like android 9.0
2. myeclipse with maven

#### 使用说明

-f  transfor an apk file to dot file
-d  transfor a directory of apk files to dot files.
-i  the input directory storing .apk files
-o  the output directory to store .dot files.
-s  the android sdk directory, like E:\android-sdk-windows\platforms.
-c  the call backs file. It can be an empty file but nessesary.
Example 1:java -jar apk2dot.jar -f -i chrome.apk -o outdir -s E:\android-sdk-windows\platforms -c cb.txt
Example 2:java -jar apk2dot.jar -d -i inputdir -o outdir -s E:\android-sdk-windows\platforms -c cb.txt

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request
