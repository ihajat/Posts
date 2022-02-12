A simple Android app that demonstrates clean code architecture. 123 124

The app should have two screens, as follows:Screen 1 should contain a list of posts. Tapping on a cell in the list, should take you to the second
screen.
Screen 2 is the detail screen for the selected post. Details to be shown about each post are:
- Post title
- Post body
- User name
- Number of comments
You get all the data you want from the following API endpoints:
- GET http://jsonplaceholder.typicode.com/posts
- GET http://jsonplaceholder.typicode.com/users
- GET http://jsonplaceholder.typicode.com/comments

![alt text](img.png)

Layers:

1. Data
2. Domain
3. Presentation

Data Layer

Contains:

1. Repository Implementations: Comments, Post, User
2. Cache Data: I am using RxPaper2

Paper is a fast NoSQL-like storage for Java/Kotlin objects on Android with automatic schema migration support.
for more information see https://github.com/pakoito/RxPaper2

3. Model: CommentEntity, PostEntity,UserEntity
4. Network: httpClient, retrofitClient, APIs: CommentsApi, PostsApi,UsersApi
5. Repository Implementations: Comments, Post, User
6. Local and remote data sources, both implementations and interfaces: CommentRemoteImpl, PostRemoteDataSourceImpl, UserRemoteImpl, etc
7. DB, eg ROOM.


Domain Layer

Contains:

1. Model
2. Repository interfaces: So, it does NOT depend on the Data layer. 
2. Usecases 


Presentation Layer

Contains:

1. Model
2. Navigation ( Controller )
3. States: dealing with loading state, etc
4. Utils: Extension functions, Intent Loaders.

Notes:

1. Presentation Layer depends on Domain Layer.
2. Presentation Layer includes views and view models/presenters.
3. Views should not contain any business logic and should only interact with Presenters (or ViewModels if you use MVVM).
4. Presenters should not contain any business logic and should talk to Domain layer via Interactors/Use Cases.
5. Domain layer contains business logic and Data layer abstractions. 

It serves as a middle-man between Presentation and Data layers yet it has no knowledge about either of them by virtue of Inversion of Control. So, it does NOT depend on the Data layer. This is because Use cases in Domain are not using the actual implementation of the Repository that sits in the Data Layer. Instead, it is just using an abstraction/interface in the Domain Layer that acts as a contract for any repository who wants to provide the data.

Business logic (the most important aspect of your app) is centralized for a good reason: for ease of maintenance, it should not be spread across many layers or, worse, duplicated in multiple places. When it comes to updating your business rules, you should not be forced to debug the whole codebase to determine how it works?—?the Domain layer is the place to look.

6. We are still applying the SOLID principles , and therefore the Repositories use Interfaces for testablity.
7. We do NOT not want to propogate the same data model throughout the whole app, rather , the concept of raw data and safe data is used, ie, each layer has its own data model.
8. sealed class for resource state: loading, success, error
9. Koin DI is used instead of Dagger
10. Caching is introduced, one of the advantages of caching , is it's lightweight, and does not require migration policies.
11. In fact,  each Repository returns data from a Data Source, eg cached or remote.
12. Combining of use cases, Post and User repositories.
9. The View Model or the Presenter will carry out the use cases.
13.The view/UI inside the presentation layer calls methods from the View Model or Presenter.
14. the user action flows from the UI all the way up to the Data Source and then flows back down. Though, This Data Flow is not the same flow as the Dependency Rule.
15. I have not carried out Instrumentation Tests on this particular example.
16. Most of the tests are Unit Tests. Though, I have NOT used TDD for this particular example.
17. Uses ktlintFormat. See https://github.com/shyiko/ktlint for more information, but, to summarise, fix all style violations automatically by running a gradle task; so , you get a lot of formatting within minutes.


Testing with Test Coverage:

Data Layer

1. Model : Test the model entities: 
	
CommentEntityTest, 
PostEntityTest, 
UserEntityTest

( 100% )

2. Local cache: 

CommentCacheImplTest, 
PostCacheImplTest, 
UserCacheImplTest

( 100% )

3. Remote Implementations

CommentRemoteImplTest, 
PostRemoteImplTest, 
UserRemoteImplTest( 100% )

4. Repository Implementations

CommentRepositoryImplTest, 
PostRepositoryImplTest, 
UserRepositoryImplTest 
( 100% )



Domain Layer

1. UseCases: CommentsUseCaseTest, UserPostUseCaseTest, UsersPostsUseCaseTest( 71% )


Presentation Layer

1. Model

CommentItemTest, 
PostItemTest

( 100% )

2. ViewModel - Post Details

PostDetailsViewModelTest
( 40% )

3. ViewModel - Post List

PostListViewModelTest
( 31% )


Please Note:
1. I have used clean code architecture example for this with Koin ( Dependency Injection framework)
2. I wanted to try new frameworks and architectures, hence, I have not completed this in the usual traditional manner.  Some of these are caching library, Koin  ( Dependency Injection framework), 
3. I have several similar repo examples for similar problem domain and using MVP/MVVM to solve them. Please look at albums/albums2, for Java/Kotlin MVVM/DataBinding/Live Data, TDD approach.
4. In this example, I have NOT used ROOM or sqlite, Realm, etc, I have several repos , with ROOM  usage
5. In this example, I have NOT used Data Binding,  I have several repos , with Data Binding usage
6. I have NOT used Dagger for DI, I have several repos , with Dagger usage
7. In this example, I have NOT used TDD, because there are several examples of code use with TDD , eg https://github.com/ihajat/Mars, https://github.com/ihajat/BowlingGame
8. In this example, I have NOT used Espresso, I have several repos , with Espresso usage

