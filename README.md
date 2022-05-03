# XkcdComics-Android
An MVP android application which displays the xkcd comics, allowing users to save their favourite comics for offline viewing. Users can also share comics with their friends as well as search for comics using either text or numbers.

## Features Implementations
In this MVP, the following features were implemented:
- browse through the comics
- see the comics details including it's description
- search for comics by the comic number as well as text
- favorite the comics, which would be available offline too
- send comic to others
- support multiple form factor (portrait and landscape design for normal screen)

## Architecture and Design Pattern
Given that this is an MVP and that we need to continually iterate, extend and add features to app, I came up with multi-module clean architecture where each feature has it own module. Overall, the app has the following modules:
- app module: The app module which integrate all the other modules together forming the whole app.
- common module: This module houses the codes that are common to all the other modules or codes that are atleast shared between two modules
- browsecomics module: This module holds the code that fetch and disply comics as the user browses through the comics
- favorite module: Holds code related to favoriting comics and viewing them offline
- search module: Holds the logic for searching comics either by number or text

#### UI Architecture Pattern
At the presentation layer of the architecture, I used MVVM with Unidirectional Data Flow (UDF) which ensure that states in the application flows from one directions and thus easier to manage and manipulate.

#### Others
- Hilt for dependencies injections
- deeplinks for navigating between modules

## Testing
Wrote units and integration tests for viewModels and repository. I didn't unit tests the usecases as that wouldn't benefit us since the usecases integration with the viewModels and repository had already been tested.

## Screenshots
<img src="https://user-images.githubusercontent.com/60139290/166424030-9ee22660-990c-4da6-933b-f274cf075e63.png" width=300/> <img src="https://user-images.githubusercontent.com/60139290/166424042-666fd763-9d11-4bd1-b8fc-51ab183642a9.png" width=300/> <img src="https://user-images.githubusercontent.com/60139290/166424053-46247590-4666-43e2-9ba9-b2ccf89ec901.png" width=300/> <img src="https://user-images.githubusercontent.com/60139290/166424062-58f21422-470b-43ed-9d00-9147369c11eb.png" width=300/> <img src="https://user-images.githubusercontent.com/60139290/166424075-50fbf1ab-1e3e-4120-a8ba-1f1f0e738083.png" width=300/> <img src="https://user-images.githubusercontent.com/60139290/166424081-48be3633-2219-4182-96f6-0cd3dd7f1dd1.png" width=300/> <img src="https://user-images.githubusercontent.com/60139290/166424094-dd4417b8-931e-4a2c-8187-54773b4d7fbe.png" height=300/>
