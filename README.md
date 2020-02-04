# Usage

### Requirements
The solution is delivered as a Java JAR file.  It requires Java JRE 8+.  

### Running the solution
If your java executable is located in your path, you can run the solution from the command line.  The solution also requires an internet connection.

### Help menu & topics list
The following will display the help menu, along with a list of topics available to crawl:
`java -jar cochrane-library-crawler.jar`

Example output:
```
usage: java -jar cochrane-library-crawler.jar
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
- `-o`: The output filename.  The output will be written to the file you specify here. (e.g. `-o ./cochrane_reviews.txt` will output to cochrane_reviews.txt in the current working directory).
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
# Problem & solution overview
The Cochrane Library consists of reviews that contain a URL, topic, title, author, and date.  The reviews are organized by topic.  Currently, there are 36 topics.  The requirement is to extract all of the reviews for at least one topic.  The solution allows a user to extract as many or as few of the topics as they require.  It always extracts all of the reviews for a given topic.

### Program flow 
The program begins at `io.rosensteel.cochrane.cli.Main`.  `args` are read from the command line and parsed using the Apache Commons CLI.  The `args` are used to create a `CrawlerSettings` object, which will contain the topics to crawl and the filename to output to.

The `Main` program then uses the `CochraneCrawler` to read each topic from the website in order.  After all topics are read, it gets the aggregate of all cached reviews from the `CochraneCrawler` and saves them to a file.

### Cochrane Library structure
Each topic links to a list of reviews for that topic.  The topic links themselves use a request that contains various parameters that Cochrane's system uses to serve the review list.

### Review count & pagination
When a topic is selected, the review list page is displayed.  The review list includes a total number of reviews found.  Often, there are more reviews than are displayed on single page.  You need to follow the *Next* page link to obtain the next set of reviews.

To assist with progress reporting and verification, the program will extract the total number of reviews and save it.  Upon processing each page of reviews, it reports the current number of reviews read out of the total available.  At the end of the process, it verifies that the total number of reviews gathered matches the number reported by the page.

### Speed & reviews per page
Each response from Cochrane's severs for a list of reviews takes several seconds.  To save time, it is best to maximize the number of reviews obtained per request.  By default, the number of reviews returned is low (25).  To obtain more reviews per page, I experimented with the `&resultPerPage` parameter.  I found that a value of 200 reliably returned the maximum number of results per page. Any higher than that, and the results became unreliable; I would often not receive the number of reviews I requested.

# Design Comments
### Generic HTTP and data extraction
A generic structure for obtaining HTTP responses and extracting data seemed like an appropriate framework to start with.  In my solution, the HTTP package serves this purpose.

- The `WebReader` is a wrapper arround an `HttpClient` pointed at a specific website.      
   - The website's main url is the `baseUri` (e.g. https://www.cochranelibrary.com in our case).  This is neccesary to properly follow links.
    - Once a `WebReader` is instantiated, you can use it to send requests and get responses from specific pages on the website (e.g. the topics page)
- `WebResult` is what is returned from a page read.
    - It is a wrapper around the plain response text and a jsoup document (referred to in the code as a dom).
    - It has a method that takes a `WebDataExtractor` and applies it to the dom.  These methods are generic, which allows us to extract data of any type from the dom.
- `WebDataExtractor` is an interface that must be implemented by a concrete class.  
    - The implementation will be specific to the page and data you are extracting from it.

### Cochrane specific components
- `CochraneCrawler` is the main class that encapsulates the work.  It delegates much of the work to `CochraneTopics`, but it contains the high-level interface and fixed data (such as URLs).

- `CochraneTopics` actually performs most of the work.  Its constructor requires the `webReader` that has been established for Cochrane's main page, plus the `topicListUrl` that it will read immediately.  It parses the topic list URL into the set of available topic names, accessible with `getAllTopicNames()`.  When `crawlTopics(String topicName)` is called, it will read the website and create the `CochraneReviews` for the specified `topicName`.  It also has a method `getReviewsForTopic(String topicName)` that will read from the cache instead if is available.  Finally, `getCrawledReviews()` will return the aggregate of all reviews available in the cache.

- `CochraneReview`, `CochraneReviews`, and `CochraneTopicLinks` are classes that encapsulate data and provide some helper functions.

- `CochraneExtractors` is an object that contains static implementations of the `WebDataExtractor` used to obtain specific data from the Cochrane website. The available extractors are: `topicLinkExtractor`, `reviewExtractor`, `nextPageLinkExtractor`, and `expectedReviewCountExtractor`.  These extractors use jsoup to obtain the data from the HTML.

### Automated testing
I used JUnit to create some automated tests.  I wanted to verify that certain functionality was working as expected, and that it did not break as I continued work on the project.

- `WebReaderTest` - This tests that exceptions are thrown when the HTTP Response status code is not 200.  It also tests that the response body is correctly read and that a jsoup dom is created.
- `CochraneExtractorsTest` - This verifies the behavior of the members of `CochraneExtractors`.
- `TestHelpers` - The `CochraneExtractors` were potentially problematic to test.  I did not want to introduce external dependencies on the Cochrane website, and I did not want to have to wait for responses in the middle of my tests.  To circumvent this issue, I extended the Apache `HttpClient` and `HttpResponse`.  I created stubs that would serve up static files that I downloaded off of the Cochrane Library website.  This way, I could be sure that my extractors worked on the actual website data without requiring an internet connection, and without introducing a dependency on the current state of the Cochrane websites.

### Exception handling
Since this is a CLI program and it has a single purpose that is likley to be used by a technical person, my philosophy was that the program should fail early and provide a detailed explaination when an exception occured.  Failing gracefully and allowing the user to continue was not a priority.  Making it easy to debug was a priority.

### Further development
I considered a few paths for continued development, but ultimately I thought it was best to wrap the project up for now.  If this were a project I was working on, I would check with the stakeholders to determine the value of the following developments.  If they would provide value for their cost, we could include them in the scope of the project.

- REST API: We could set up a server to respond to POST requests for the various topics.  We would run the same process to acquire the reviews, but we would return them as HTTP responses instead of as a flat file written to the file system.
- Runnable executable: It would be possible to create an executable to run on Windows or Linux based operating systems.  Currently the Jar is useful because it is cross-platform.
- CI/CD: It would be possible to build a CI/CD solution that would run automated tests, build the project, and publish artifcats (JAR, documentation, deploying REST API to a server, etc...)
- Multithreading: This would be useful for speeding up the download when multiple large topics are required.  The bottleneck is the server response, so we could make multiple requests at the same time and wait for them to return.  I experimented with this already, but I found that the results were unreliable.  The website would occasionally not return complete results for large topics.  Additional debugging could resolve this issue.
- GUI: If desired, a we could create a Swing UI or single page web app front end.  If we had a REST API, we could build it on top of that.

# Dependencies
The following open source libraries were used in this project:
- [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Apache HTTP](https://hc.apache.org/httpclient-3.x/)
- [jsoup](https://jsoup.org/)
- [JUnit](https://junit.org/junit5/)
