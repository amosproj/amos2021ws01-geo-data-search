# Documentation
The project documentation can be found in the Wiki:

* [User Documentation](https://github.com/amosproj/amos2021ws01-geo-data-search/wiki/User-Documentation)
* [Design Documentation](https://github.com/amosproj/amos2021ws01-geo-data-search/wiki/Design-Documentation)
* [Build Documentation](https://github.com/amosproj/amos2021ws01-geo-data-search/wiki/Build-Documentation)



## Backend code structure

**com.example.backend**
	BackendApplication
		_contains our main-method as a starting point_
	**.clients**
		_contains all FeignClients_
		_API and NLP_
	**.controllers**
		_contains all Controllers_
		_API and Frontend_
	**.data**
		_contains all data structures_
		_ApiResponses, HttpResponses, NlpResponse and its details_
	**.helpers**
		_useful helper classes/methods_
		_BackendLogger_

## Features
**Routing** - when receiving the `NlpQueryResponse` the Backend will check the key `query_object`at first, to decide which API to use for further searching. When receiving `query_object="route"` the HERE API will be chosen. Therefor (and for now), we always start our Routing in Berlin at the Brandenburger Tor and use the key `location`from the NLP-Response as our ending point. The Backend will send the value of this key to the **HERE API GEOCODE** to retrieve the coordinates for this location. Then we send both coordinates to the **HERE API GUIDANCE**. The result will be then passed to the Frontend.

**Routing attributes** - the user can set additional attributes into their query. When there is a need for charging stations along the route, this can be entered within the query. Additional to that we offer the avoidance of toll roads. Therefor the `NlpQueryResponse` contains two specified keys `charging_stations`and `toll roads`. When set to TRUE, the routing will be adjusted accordingly. Regarding the charging stations: Their positions will be marked on the map in the Frontend.
