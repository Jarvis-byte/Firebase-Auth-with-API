
Firebase-Auth-with-API
===================================

This app displays a list of data from the Api 'https://reqres.in/api/users?page=1' into a recycler view , sorted alphabetically.

These has also Firebase Email Authentication and Firebase Realtime Database to verify and store personal informations.



![Firebase Auth With API demo](20201103_115647.gif)



Pre-requisites
--------------

You have to connect with internet to use the app or a alert dialouge with no internet will pop up when we try to press login button or sign in button.

If you are logged in and havenot logged out the last time then the alert dialuge will pop out at the starting of the app.


Getting Started
---------------

  * For New Users
-------------------

        First create an account by clicking on the plus icon . Give all you details as per the fields required.
        In case you forget to input a value in the field, you will get an error message to fill all the details properly.
        After that press sign in to create your account and you will directly go the home page.

<img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/New User.jpeg" width="300"/>

  * For Users' Who Have An Account
-------------------
 If you  did not logged out the last time using the app,then you will directly go the 'Home Page'.
 If you have logged out the last time then you have to give the email-id and password to login.

<img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/Before Login.jpeg" width="300"/> <img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/Login After.jpeg" width="300"/> 

Home Page
-------

After the above steps, you are now on your 'Home Screen' where you can see bunch of lists with personal information of many differnet persons like
Id,First Name, Last Name,Email-Id and picture of that person.

<img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/Loading data.jpeg" width="300"/> 

Long press on any of the list items to get to delete the information. A dialouge box asking you to confirm will appear, click 'YES' to delete 'NO' to cancel the task.

<img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/Delete Dialouge.jpeg" width="300"/> <img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/Deleted Toast.jpeg" width="300"/> 

You have a navigation menu which you can see either by swiping right from left or by press on the navigation button on top left corner of the screen.
In the top of the navigation button you can see all the information about youself which you have entered at the time of registration.

<img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/Navigation Pannel.jpeg" width="300"/>

At the buttom of the pannel you have 'LOG OUT" button, press and you will get an alert dialouge with 'YES' or 'NO' options. Press on 'YES' to log out or to cancel the activity click 'NO'.

<img src="https://github.com/Jarvis-byte/Firebase-Auth-with-API/blob/master/Log Out.jpeg" width="300"/>





