export CLASSPATH=.:/home/rango/ITU/log/jar/servlet-api.jar
source ~/.bashrc
cd frakawork
javac -parameters -d . *.java 
chmod 777 -R ./etu1757
jar -cvf ../company/WEB-INF/lib/etu1757.jar etu1757
cp ../company/WEB-INF/lib/etu1757.jar  /home/rango/ITU/log/jar/
cd ../company/WEB-INF/classes/
cp /home/rango/ITU/log/jar/postgresql-42.5.0.jar ../lib/
export CLASSPATH=.:/home/rango/ITU/log/jar/etu1757.jar
source ~/.bashrc
javac -parameters -d . *.java 
cd ../../
jar -cvf ./company_v2.war .
cp ./company_v2.war /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/webapps/
sudo /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/bin/shutdown.sh
sudo /home/rango/ITU/log/ognu/tomcat/apache-tomcat-10.0.27/bin/startup.sh

