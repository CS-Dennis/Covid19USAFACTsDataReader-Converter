# Covid19 USAFACTs Data Reader and Converter

# Description:
Data reader and converter of covid 19 U.S. confirmed cases by state. Data source: [facts.org/visualizations/coronavirus-covid-19-spread-map/](https://usafacts.org/visualizations/coronavirus-covid-19-spread-map/)
This GUI application is written in Java.

# Purpose:
If you are working with covid 19 U.S. data, especially with confirmed covid 19 cases from usafacts.org. This application will be able to read the data file in CSV format and consolidate all confirmed cases of all counties by each state (50 of them) plus D.C.; export the results in CSV format. In this application, it has a direct link to the source file and the web page for you to view and download by yourself. You can choose to export the data of all dates from the first date that was recorded on 1/22/2020 until the last date the data has been recorded; or you can choose a specific date to export.

## Compiled java application ready for use on Windows:
You can download the application directly [here](https://drive.google.com/file/d/1kASPYLDo3KfyqOWBtolLh929JkZV_icD/view). Please make sure you have java runtime environment installed on system ([https://www.java.com/en/download/](https://www.java.com/en/download/)).

## How to use the compiled application:
Once you extract all the folders and files from the Covid19DataReader&Converter.zip file, open folder bin and launch the batch file App.bat. Ready to enjoy.

![Main Screen]( https://drive.google.com/uc?export=download&id=1BhrAAmOWsrzJ7k1BeXv8w5YAOqS_gUsa | width=100)



# Detail of the Java Project:
This project is a JavaFX Maven project that mainly consists of four Java classes: App.java, CovidDataReader.java, CovidStateDaily.java, and MainController.java

## Description of the Classes:
### The App.java:
It has the main function for launching the java program.

### CovidStateDaily.java:
A class define the objects of covid daily status by state.
It has four fields including state, date, caseNum, and stateFIPS.

### CovidDataReader.java:
The meat and potatoes of this program. It defines functions of how to process the original data file and export the results based on the user selection of dates.

### MainController.java
A controller class of the graphic user interface for this program.

## Contact the Author for any questions.
If you have questions, welcome to leave comments here or contact via ynyz2003@gmail.com Nan Ye

# Thanks again for being interested in this application!
