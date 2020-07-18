package org.covid19.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// covid data reader for data from usafacts.org; the data format should be CSV (*.csv) type.
// data page: https://usafacts.org/visualizations/coronavirus-covid-19-spread-map/
// data direct link: https://usafactsstatic.blob.core.windows.net/public/data/covid-19/covid_confirmed_usafacts.csv
public class CovidDataReader {
	private String dataURL;
	private BufferedReader bfr;
	private ArrayList<CovidStateDaily> covidStateDailyList;
	
//	arraylist of export data line without the first title line
	private ArrayList<String> exportStringLineAllDate;
	private ArrayList<String> exportStringLineOneDate;
	
	public CovidDataReader(String dataURL) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		this.dataURL = dataURL;
		this.bfr = new BufferedReader(new FileReader(this.dataURL));
	}
	
//	get total daily case number for each state by date
	public ArrayList<String> readDataByDate(String date) {
		String[] firstLine;
		int dateIndex;
		this.covidStateDailyList = new ArrayList<CovidStateDaily>();
		try {
			firstLine = this.bfr.readLine().split(",");
			dateIndex = 0;
			
//			due to the data deficiency: the year of the dates are changing back and forth from 20 to 2020
//			determine the year format: if data year format is 20, change the input date year format from 2020 to 20
			String year = firstLine[4].split("/")[2];
			if(year.equals("20")) {
				date = date.split("/")[0]+"/"+date.split("/")[1]+"/20";
			} else if(year.equals("2020")) {
				date = date.split("/")[0]+"/"+date.split("/")[1]+"/2020";
			}
			
			for (String fieldName : firstLine) {
				if(fieldName.equals(date)) {
					break;
				}
				dateIndex++;
			}
			
			
			String temp = "";
			int totalNationalDailyNum = 0;
			int stateFIPS = 0;
			String stateName = "";
			int stateTotalDailyNum = 0;
			
			int dataFileLineNum = 1;
			while ((temp=this.bfr.readLine())!=null) {
//				current line number of the data CSV file
				dataFileLineNum++;
				
				String[] tempArray = temp.split(",");
				if(stateName.equals("")) {
					stateFIPS = Integer.parseInt(tempArray[3]);
					stateName = tempArray[2];
					stateTotalDailyNum += Integer.parseInt(tempArray[dateIndex]);
				} else if (stateName.equals(tempArray[2])) {
					stateTotalDailyNum += Integer.parseInt(tempArray[dateIndex]);
					if(dataFileLineNum==3196) {
						CovidStateDaily covidStateDaily = new CovidStateDaily();
						covidStateDaily.setStateFIPS(stateFIPS);
						covidStateDaily.setState(stateName);
						covidStateDaily.setDate(date);
						covidStateDaily.setCaseNum(stateTotalDailyNum);
						this.covidStateDailyList.add(covidStateDaily);
					}
				} else if(stateName.equals(tempArray[2])==false) {
					CovidStateDaily covidStateDaily = new CovidStateDaily();
					covidStateDaily.setStateFIPS(stateFIPS);
					covidStateDaily.setState(stateName);
					covidStateDaily.setDate(date);
					covidStateDaily.setCaseNum(stateTotalDailyNum);
					this.covidStateDailyList.add(covidStateDaily);
					
					stateFIPS = Integer.parseInt(tempArray[3]);
					stateName = tempArray[2];
					stateTotalDailyNum = Integer.parseInt(tempArray[dateIndex]);
				}
				
				
				
				totalNationalDailyNum += Integer.parseInt(tempArray[dateIndex]);
				
			}
			
			int t = 1;
			this.exportStringLineOneDate = new ArrayList<String>();
			for (CovidStateDaily covidStateDaily : this.covidStateDailyList) {
				String eachLine = t+","+covidStateDaily.getState()+","+covidStateDaily.getStateFIPS()+","+covidStateDaily.getCaseNum();
				this.exportStringLineOneDate.add(eachLine);
				t++;
			}
			this.exportStringLineOneDate.add(t+","+"Total,"+","+totalNationalDailyNum);
			
			return this.exportStringLineOneDate;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return null;
		} catch (NullPointerException e) {
			// TODO: handle exception
			System.out.println(e);
			return null;
		}
	}

	
//	get all dates for each state
	public ArrayList<String> readDataAllDates() {
		String[] firstLine;
		this.covidStateDailyList = new ArrayList<CovidStateDaily>();
		
		try {
//			get the first line for export: id, state, stateFIPS, dates... 
			firstLine = bfr.readLine().split(",");
			String titleLine = "id,";
			
			for (int i = 2; i < firstLine.length; i++) {
				if(i<=firstLine.length-2) {
					titleLine += firstLine[i]+",";
				} else {
					titleLine += firstLine[i];
				}
				
			}
			
			this.exportStringLineAllDate = new ArrayList<String>();

			
//			get each date from the data file, index i start at 3 for the fisrt date col
			for (int i = 3; i < titleLine.split(",").length; i++) {
				bfr = new BufferedReader(new FileReader(new File(dataURL)));
				this.readDataByDate(titleLine.split(",")[i]);
				if (i==3) {
//					exprotStringLineOneDate arraylist<String> format: id,state,stateFIPS, CaseNumofEachDate
					for (String line : this.exportStringLineOneDate) {
						this.exportStringLineAllDate.add(line);
					}
				} else {
					for (int j = 0; j < exportStringLineOneDate.size(); j++) {
						this.exportStringLineAllDate.set(j, this.exportStringLineAllDate.get(j)+","+this.exportStringLineOneDate.get(j).split(",")[3]);
					}
				}
			}
			
			this.exportStringLineAllDate.add(0, titleLine);
			
			for (int i = 0; i < this.exportStringLineAllDate.size(); i++) {
				this.exportStringLineAllDate.set(i, this.exportStringLineAllDate.get(i)+"\n");
			}
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.exportStringLineAllDate;
	}
}
