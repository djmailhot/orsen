
# AWS Volume snapshot for CSE454 data corpus
# snap-1dba7924


# REDHAT download and install sbt from an rpm repo
#sudo rpm -ivh http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt//0.12.2/sbt.rpm

# UBUNTU
wget http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt//0.12.2/sbt.tgz
tar -xvf sbt.tgz
sudo mv sbt/bin/* /usr/local/bin/
rm -r sbt*

# download and install mongodb binarys
wget http://fastdl.mongodb.org/linux/mongodb-linux-x86_64-2.2.3.tgz
tar -xvf mongodb-linux-x86_64-2.2.3.tgz
sudo mv mongodb-linux-x86_64-2.2.3/bin/* /usr/local/bin/
rm -r mongodb-linux-x86_64-2.2.3*

# install java
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get install oracle-java7-set-default


# get our repo
sudo apt-get install git
git clone https://github.com/djmailhot/orsen.git


