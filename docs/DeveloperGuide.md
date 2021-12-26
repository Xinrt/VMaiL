# Development

Welcome to the section of the documentation covers information for developers.

Changes are documented in the git of this project.

If you have any comments please feel free to [provide feedback](https://gitlab.com/teamsixer/vmail/-/issues/new).

## 1. Getting Started

To develop VMaiL you need a working understanding of:

- Kotlin
- XML
- Andorid

 With just kotlin knowledge you may be able to make minor logic changes and small fixes. But the Android APIs are used extensively (as well as Android based libraries), particularly in the front-end.

XML provides the translation (however that’s pulled in from Transifex anyway) and some of UI layout.

Once you’ve gained a solid understanding of these we recommend you look at our existing issues and pick one tagged ‘good first issue’. If there’s not a tagged one not being worked on then feel free to pick an issue that interests you. If there’s no developer comments on the issue you select (and it’s not marked beginner) feel free to ask about the best approach before you start!

To build the project simply clone the repository and import the project into [Android Studio](https://developer.android.com/studio/index.html). From there you can use the IDE tools to build the VMaiL source code and install it. Make sure following the guidance in README.md to build source code.



## 2. Architecture

The general structure of VMaiL is as follows:

### Modules

- **app** - Main module - includes code for activities, database interaction, settings, and voice interaction.
- **library** - Email core code for contacting mail providers and decoding emails from MIME. This library is customized by our team. Origin repository please check https://github.com/mailhu/emailkit



### Core Design

#### Design Pattern

State design pattern and strategy design pattern.

- **State Pattern** - Different states will behave differntly. This pattern can achieve the speaking context with users.
- **Strategy Pattern** - Different tasks will be called depending on the user input in current state.



#### Layering

DAO(Local), Network(Network) - > Repository -> ViewModel -> Activity

- **DAO**  -  method directly manipulate SQLite database. VMaiL project uses [LitePal framework](https://github.com/guolindev/LitePal) for database manipulation.
- **Model** - The model is matching the actual table in SQLite databse. Please refers to LitePal Framework to see how model works.
- **Netowork** - Network layer provides the functions to initiate a request to server.
- **Repository** - Repository layer uses the APIs provided by DAO and network layer, and encapsulates all functions needed to provide APIs for ViewModel layer.
- **ViewModel** - ViewModel provides APIs for activity layer and help activity do computation work. 
- **Activity** - Activity layer focus on the interaction of UI and voice. The state of activity are changed when the context changes.
- **State** - State is the core part to achieve the context switching in voice interaction. Different state means different contexts. State has different tasks to perform based on the user interaction with current state.
- **Task** - Task belongs to state. Each task encapulastes the detail works for each state.
- **Utils** - Provides utility functions.

#### Voice Interaction

- **AsrSupport (com.teamsixers.vmail.ui.voiceSupport.AsrSupport)** -  AsrSupport provides functions for auto speech recognition. Activity needs to have function of auto speech recognition should implement this interface.
- **PassiveListeningSupport (com.teamsixers.vmail.ui.voiceSupport.PassiveListeningSupport )** - PassiveListeningSupport provides functions for wake up listening. The hot word "V Mail" will call the function that you implement in `mWakeUperListener`.
- **TextToSpeechSupport (com.teamsixers.vmail.ui.voiceSupport.TextToSpeechSupport  )** - Provides the function to read the text you input.



## 3. Third Part Library Used

- **Android-SmartRefresh library** - Library for implementing ‘pull to refresh’ lists. https://github.com/scwang90/SmartRefreshLayout
- **Andorid SQLite Framwork** - LitePal provides easy way to manipulate SQLite database. https://github.com/guolindev/LitePal
- **EmailKit** - Provides core APIs for contacting email providers. This framework has been customized by our team. https://github.com/mailhu/emailkit
- **JavaMail** - The *JavaMail* API provides a platform-independent and protocol-independent framework to build mail and messaging applications. https://javaee.github.io/javamail/
- **MicroKV** - Provides encryptions for user account and password in local storage. https://github.com/mailhu/microkv
- **Mockk** - Testing Framework for mocking object for testing. https://github.com/mockk/mockk
- **Jsoup** -  the Java HTML parser, built for HTML editing, cleaning, scraping, and XSS safety. https://github.com/jhy/jsoup
- **Essence** - Automatically extract the main text content (and more) from an HTML document. https://github.com/cdimascio/essence
- **AndoridUtil** -  Android developers should collect the following utils(updating). https://github.com/Blankj/AndroidUtilCode
- **PermissionX** - An open source Android library that makes handling runtime permissions extremely easy.https://github.com/guolindev/PermissionX
- **Fuzzywuzzy** - Java fuzzy string matching implementation of the well known Python's fuzzywuzzy algorithm. Fuzzy search for Java. https://github.com/xdrop/fuzzywuzzy



#### 