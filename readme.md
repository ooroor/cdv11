# CDV11: Utility for validating and creating cdv11 valid strings and its usages fnr, dnr and bnr

## Intention

### This _IS_...:
- ... a collection of reusable classes not needed to be tweeked to be used
- ... to become a library
### This is _NOT_...:

### Usage
```
final Fnr fnr = new Fnr("21042221454");
```
```
try {
    final Fnr fnr = new Fnr("21042221455");
} catch (Cdv11StringFormatException | DateBasedCdv11StringFormatException e) {
    log.error(e.toString());
}
```
### Future enhancements
- Random generation
- Create from localDate and counter
### Writing your own code

### Useful commands
- ```mvn versions:display-dependency-updates``` 
# Refs: