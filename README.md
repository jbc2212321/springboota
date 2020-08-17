## 3 分钟了解存量SpringBoot应用迁移到云开发平台

### 1、您能看到这篇文章，说明已经创建的一个应用，并且已经打开了CloudIDE。是的，第一步，就是需要创建一个云应用，然后打开CloudIDE；

### 2、把存量的SpringBoot应用迁移到云开发目录下： 拖动工程根目录下的src、pom.xml 到CloudIDE工程目录下，就能完成迁移。

### 3、修改pom.xml, 以满足云开发平台构建镜像的规范:
```
   <properties>
        <applicationName>${project.artifactId}</applicationName>
   </properties>
   ...
   <build>
      <finalName>${applicationName}</finalName>
      <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>${spring-boot.version}</version>
            <configuration>
                <mainClass>com.alibaba.sca.temp.web.Application</mainClass>
                <layout>ZIP</layout>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
      ...
    </build>
```
   最好再加上java编译版本，1.8：
```
   <build>
      <plugins>
           <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            ...
        </plugins>
    </build>
```
    
### 4、到这里已经迁移完毕。需要验证一下是否迁移成功。
    1、CloudIDE-Native启动：在cloudide的右下角Termial中的命令行中，输入启动命名：mvn spring-boot:run ,验证是否能在IDE容器中启动成功，启动成功后在IDE左下角有一个“预览”功能，可以把云端IDE启动的服务在本地浏览器中访问到，则说明Cloud-Native配置是OK的。
    2、提交代码到CodeUp上；
    3、开始部署到线上环境。在IDE左边工具栏中打开云开发插件，然后点击部署按钮，开始部署到云端。如果部署成功，则会在输出日志中，打印一个临时域名，可以直接访问。 
    到这一步，存量web应用就算迁移完了。


   
    