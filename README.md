# Excel to Sie
JavaFX app to create a si-file for import of Gross pool (Bruttopool) data
into Accounting system (fortnox).
## build with maven
```
mvn clean install 
```
## package with maven
Create an executable jar with dependencies
exceltosie-<verision>-jar-with-dependencies.jar
```
mvn clean install package
```

## run 
```
>double click jar with dependencis
```
## Excel input file requirements
* The accounting data sheet is named by year (2108, 2019 ...)
* An additonal sheet called SI-fildata exists. 

See example testfil.xlsx
