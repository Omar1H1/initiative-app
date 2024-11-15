```mermaid
flowchart LR
    subgraph 'account init page'
    uc1((access code))
    uc2((account initiation))

    end
    user[user]
    admin[Admin]


    admin --Give--> user
    user --use--> uc1
    uc1 -- to access --> uc2
```

```mermaid
flowchart LR
    subgraph 'home page'
    uc1((logged in))
    uc2((profiles list))

    end
    user[user]
    admin[Admin]

    user -- should be --> uc1
    uc1 -- to access --> uc2
```

```mermaid
flowchart LR
    subgraph 'Other Profile'

    uc2((Profile))

    end
    user[user]
    admin[Admin]

    user --Request / accept--> uc2

```

```mermaid
flowchart LR
    subgraph 'Other Profile'
        uc1((Send a message))
        uc2((Unmatch))

    end
    user[user]
    admin[Admin]

    user -- Already Matched--> uc1
    user --Already Matched--> uc2

```

```mermaid
flowchart LR
    subgraph 'Profile'
    uc1((My list of demandes))
    uc2((inbox))
    uc3((Espace docs))
    end
    user[user]
    admin[Admin]


  
    user --see--> uc1
    user --access--> uc2
    user --access--> uc3
```


![img.png](img.png)

![img_1.png](img_1.png)

```mermaid
sequenceDiagram
    unknown User->>Frontend: I forgot my password, can i reset it ?
    Frontend ->> unknown User: of course you can, here this page give me your email 
    unknown User ->> Frontend: Here is my email : test@test.fr
    Frontend ->> Backend: Hello Backend, by any chance do you have a user with this email "test@test.fr"
    Backend ->> Database: User with this email "test@test.fr" is a user ?
    Database ->> Backend: Yes, don't ask too much stupid quetions please i am busy
    Backend ->> Frontend: so Mr Database is a little bit angry, but yes this user exists in our records
    Frontend ->> Backend: so can i ask you to delete his old password and send a new email so he can give a new password ?
    Backend ->> Database: sooooo, i am sorry but can you delete the password of the user who has this email "test@test.fr" .... !!
    Backend ->> Frontend: Yes i can, and i just did, i am speed ⚡⚡⚡
    Frontend ->> Backend: let's slow down with the memes lightning mcqueen
    Frontend ->> unknown User: So we just sent an email with a link to reset your password
    
```