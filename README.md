

# Getting Round SSL Problem

## Get a Self-Signed Certificate
 * Browse the server using Firefox
 * Right click -> View Page Information
 * Choose security
 * View Certificate
 * Details
 * Export it!
 * Save it

## Import a Self-Signed Certificate
```sudo keytool -importcert -file ${HOME}/hdp24sandbox.crt -alias hdp24sandbox -keystore /usr/lib/jvm/java-8-oracle/jre/lib/security/cacerts```

Default password is `changeit`
