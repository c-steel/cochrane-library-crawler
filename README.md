# Usage

### Requirements
The solution is delivered as a Java JAR file.  It requires Java JRE 8+.  

### Running the solution
If your java executable is located in your path, you can run the solution from the command line.  The solution also requires an internet connection.

### Help menu & topics list
The following will display the help menu, along with a list of topics available to crawl:
`java -jar cochrane-library-crawler`

Example output:
```
usage: java -jar cochrane-crawler
 -h         Display this help information
 -o <arg>   [Required] - Output filename. [-o ./cochrane_reviews.txt]
 -t <arg>   [Required] - Input topic numbers to crawl.  Comma delimited.
            Use dash for ranges.  [-t 0-5,10,24]
Available topics....
0. Lungs & airways
1. Methodology
2. Mental health
//.... 30 lines ...//
33. Orthopaedics & trauma
34. Neonatal care
35. Cancer
```
### Crawling the Cochrane Library
To actually crawl the library, two arguments are required:
- `-o`: The output filename.  The output will be written to the file you specify here. (e.g. `-o ./cochrane_reviews.txt` will output to cochrane_reviews.txt in the current working directory.)
- -`t`: The topic numbers to crawl.  Refer to the help menu to determine which topic number maps to which topic.  (e.g. `-t 0` will crawl "Lungs & airways").  To crawl multiple topics, you can separate topic numbers with commas `-t 1,2,3`.  You can also specify ranges with a dash `-t 0-15`.


Example call:
`java -jar ./cochrane-library-crawler.jar -t 1,25-26,35 -o ./cochrane_reviews.txt`

Example command line output:
```
(Methodology) Progress:35/35
(Methodology) Finished:35/35
(Wounds) Progress:163/163
(Wounds) Finished:163/163
(Health professional education) Progress:2/2
(Health professional education) Finished:2/2
(Cancer) Progress:200/743
(Cancer) Progress:400/743
(Cancer) Progress:600/743
(Cancer) Progress:743/743
(Cancer) Finished:743/743
```
Example file output (e.g. "./cochrane_reviews.txt"):
```
https://wwww.cochranelibrary.com/cdsr/doi/10.1002/14651858.MR000016.pub3/full|Methodology|Editorial peer review for improving the quality of reports of biomedical studies|Tom Jefferson, Melanie Rudin, Suzanne Brodney Folse, Frank Davidoff|18 April 2007
https://wwww.cochranelibrary.com/cdsr/doi/10.1002/14651858.MR000010.pub3/full|Methodology|Grey literature in meta‐analyses of randomized trials of health care interventions|Sally Hopewell, Steve McDonald, Mike J Clarke, Matthias Egger|18 April 2007
//.... 939 lines ....//
https://wwww.cochranelibrary.com/cdsr/doi/10.1002/14651858.CD002805.pub2/full|Cancer|Cranial irradiation for preventing brain metastases of small cell lung cancer in patients in complete remission|The Prophylactic Cranial Irradiation Overview Collaborative Group|6 February 2018
https://wwww.cochranelibrary.com/cdsr/doi/10.1002/14651858.CD008427.pub3/full|Cancer|Interventions for fatigue and weight loss in adults with advanced progressive illness|Cathy Payne, Philip J Wiffen, Suzanne Martin|7 April 2017
```
# Problem & Solution Overview
### Cochrane Library extracted data
### Speed & query format
# Design Comments
### Automated testing
### General HTTP and data extraction
### Cochrane specific components

# Dependencies
The following open source libraries were used in this project:
- [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Apache HTTP](https://hc.apache.org/httpclient-3.x/)
- [jsoup](https://jsoup.org/)
- [JUnit](https://junit.org/junit5/)
