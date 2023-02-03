# Login and Authentication

1. Login

Use OKHttp to implement the login in feature

Post token in header and account & password in body to backend
After verify successfully, store token ,account and password in local
## test with postman
url:{{url}}/authenticate/login
Post: 
```txt
Header:
"appSecret":123456
"Content-Type":application/json;charset=UTF-8

Boday:
{
    "account":"Tom",
    "password":"123123"
}
```
Postman mock server

```txt
Header: 
"appSecret":123456
"Content-Type":application/json;charset=UTF-8
```

```txt
{
    "code":200,
    "msg":"login Successfully",
    "data":{
        "access_token":"123456",
        "expires_in":"720"
    }
}
```

2. LoginActivity

- initView()
    - output() to retrieve the data(password and account)
- Login()
    - send token and userInfo to backend verify
    - ...will add user entity for future
    - save token if verify successfull and store userinfo(account and password) 
    -jump to mainactivity for test purpose( need to modify for future)





