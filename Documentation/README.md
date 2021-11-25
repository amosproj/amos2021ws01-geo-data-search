Build, user, and technical documentation
Software architecture description

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