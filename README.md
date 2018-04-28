# JSock Core

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

JSock Core is a client-server message passing library that simplifies TCP/UDP communication  
The motivation for JSock Core was to remove the complexity of socket communication and allow  
developers to create stunning realtime applications with ease

### Features
- Socket listening
- Message sending
- Update broadcasting
- Channels
- Connected client and channel management
- Comprehensive client & server logging
- Highly configurable 

### Projects using JSock Core
- [hcipher-chat](https://github.com/kyleruss/hcipher-chat)
- [jsock-chat](https://github.com/kyleruss/jsock-chat)

### Prerequisites
- JDK 1.8+
- Maven 3.3+

## Installation
- Clone the jsock-core repository
```
git clone https://github.com/kyleruss/jsock-core.git
```
- Build the client, core and commons  
Run the following maven command in each of the `client`, `core` and `commons` directories  
This will compile the core modules and create JAR packages in the target directory
```
mvn package
```
- Include the JSock Core libraries in your project  
You will need to include the commons package in both the client and server component of your project  
The server package needs to be referenced in your server component and the client package in your client component

## License
JSock Core is available under the MIT License  
See [LICENSE](LICENSE) for more details
