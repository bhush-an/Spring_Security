# Spring_Security

### **In-Memory Spring Security**  
- **Definition**: Authentication and user details are stored directly in memory during runtime.  
- **Use Case**: Suitable for small applications, testing, or prototyping.  
- **Configuration**:
  - Users and roles are hardcoded in the `SecurityConfig` class.
- **Advantages**: Simple and quick setup.  
- **Limitations**: Not suitable for production due to lack of scalability and persistence.

---

### **DAO-Based Spring Security**  
- **Definition**: Authentication and user details are fetched from a database using a **Data Access Object (DAO)** layer.
- **Use Case**: Ideal for production applications requiring persistent and dynamic user data.
- **Configuration**:
  - Uses a `UserDetailsService` implementation to load user data from the database.
- **Advantages**: Scalable, dynamic, and production-ready.  
- **Limitations**: Requires a database and more complex setup.

---

**Key Difference**:  
- **In-Memory** is static and hardcoded, best for testing.  
- **DAO-Based** uses a database for dynamic, persistent user authentication in production.
