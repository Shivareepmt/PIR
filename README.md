# PIR

How to install azure cognitive?

1. Install Apache Maven. Then run "mvn -v" to confirm successful installation. https://maven.apache.org/install.html
2. Install the Speech SDK and dependencies: run "mvn clean dependency:copy-dependencies"
3. Set environement variables:
run "setx SPEECH_KEY your-key"
run "setx SPEECH_REGION your-region"
To set the SPEECH_KEY environment variable, replace your-key with a7e40487a1ff409d8dc1994952b90e01
To set the SPEECH_REGION environment variable, replace your-region with westeurope

4. restart to make env changes effective

