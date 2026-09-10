# E-Commerce REST API Platform

A production-grade backend API for complete e-commerce platform built with Spring Boot.

**Live Demo:** 
**GitHub:** https://github.com/lalitsingh712/e-commerce

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Key Achievements](#key-achievements)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Deployment](#deployment)
- [Author](#author)

---

## ⭐ Features

### Core E-Commerce Features
- **Product Management**: Create, read, update, delete products
- **Shopping Cart**: Add/remove products, manage quantities
- **Order Management**: Complete order lifecycle with tracking
- **User Management**: Registration, authentication, profiles
- **Payment Processing**: Payment integration ready

### Advanced Features
- **Advanced Product Search**
    - Multi-parameter filtering (keyword, price range, rating, category, stock)
    - Real-time search with pagination
    - Dynamic sorting by any field

- **Order Tracking System**
    - Real-time status tracking (CONFIRMED → PROCESSING → SHIPPED → DELIVERED)
    - Tracking numbers for each order
    - Automatic stock refunding on cancellation

- **Product Reviews & Ratings**
    - 1-5 star rating system
    - User comments and feedback
    - Automatic average rating calculation
    - One review per user restriction

### Security Features
- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: CUSTOMER and ADMIN roles
- **Password Security**: BCrypt hashing
- **API Authorization**: Method-level access control

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.1.0 |
| **Security** | Spring Security, JWT |
| **Database** | MySQL 8.0, Hibernate ORM |
| **API** | Spring Data JPA, REST, Swagger/OpenAPI |
| **Testing** | JUnit 5, Mockito |
| **Build** | Maven 3.6+ |
| **Version Control** | Git/GitHub |

---

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Git
- Postman (for API testing)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/lalitsingh712/e-commerce.git
cd e-commerce
```

2. **Setup Database**
```bash
mysql -u root -p
CREATE DATABASE e-commerce_db;
EXIT;
```

3. **Configure Application Properties**
```bash
# Copy template
cp src/main/resources/application.properties.example src/main/resources/application.properties


```

4. **Build Project**
```bash
mvn clean install
```
# Update spring.datasource.username and spring.datasource.password
# with your local MySQL credentials.

5. **Run Application**

**Option A: IntelliJ IDE**
- Open `src/main/java/com/lalit/e_commerce/ECommerceApplication.java`
- Click green ▶ button or press Shift + F10

**Option B: Terminal**
```bash
mvn spring-boot:run
```

6. **Access Application**
- Swagger UI: http://localhost:8080/swagger-ui/index.html

---

## 📚 API Endpoints

### Authentication

POST /api/auth/login # Login & get JWT token
POST /api/auth/register # Register new user


### Products

GET /api/products # Get all products
GET /api/products/{id} # Get product by ID
GET /api/products/search?keyword=mouse # Search products
GET /api/products/search/advanced?keyword=&minPrice=&maxPrice=&categoryId=&minRating=&inStock= # Advanced search
POST /api/products # Create product (ADMIN only)
PUT /api/products/{id} # Update product (ADMIN only)
DELETE /api/products/{id} # Delete product (ADMIN only)
GET /api/products/category/{categoryId} # Get products by category


### Shopping Cart

GET /api/cart/user/{userId} # Get user's cart
POST /api/cart/add # Add item to cart
PUT /api/cart/update # Update cart item
DELETE /api/cart/item/{itemId} # Remove from cart
DELETE /api/cart/clear/{userId} # Clear entire cart


### Orders

POST /api/orders/user/{userId}?shippingAddress=... # Place order
GET /api/orders/{orderId} # Get order details
GET /api/orders/user/{userId} # Get user's orders
PUT /api/orders/{orderId}/status?status=SHIPPED # Update order status
DELETE /api/orders/{orderId}/cancel # Cancel order


### Reviews

POST /api/reviews/product/{productId}/user/{userId} # Add review
GET /api/reviews/product/{productId} # Get product reviews
DELETE /api/reviews/{reviewId}/user/{userId} # Delete review


### Categories

GET /api/categories # Get all categories
GET /api/categories/{id} # Get category by ID
POST /api/categories # Create category (ADMIN only)
PUT /api/categories/{id} # Update category (ADMIN only)
DELETE /api/categories/{id} # Delete category (ADMIN only)


### Users

GET /api/users/{id} # Get user profile
PUT /api/users/{id} # Update user profile
GET /api/users # Get all users (ADMIN only)


---

## 🎯 Key Achievements

✅ **40+ REST Endpoints** - Complete API coverage  
✅ **Advanced Search** - Multi-parameter filtering with Criteria API  
✅ **Transaction Safety** - @Transactional with automatic rollback  
✅ **Real-time Tracking** - Order status workflow with tracking numbers  
✅ **Automated Testing** - JUnit-based unit tests for DTO and entity behavior
✅ **Swagger Documentation** - Complete API documentation  
✅ **JWT Security** - Token-based authentication  
✅ **Role-Based Access** - CUSTOMER and ADMIN authorization  
✅ **Global Exception Handling** - Proper HTTP status codes  
✅ **Production-Oriented Architecture** - Layered design, logging, exception handling, and transaction management

---

## 📊 Database Schema

### Key Entities
- **User** - User profiles with authentication
- **Product** - Product catalog with ratings
- **Category** - Product categories
- **Cart** - Shopping cart management
- **Order** - Order tracking and management
- **OrderItem** - Individual items in orders
- **Review** - Product reviews and ratings
- **Payment** - Payment information

### Relationships

User (1) ----< (Many) Order
User (1) ----< (Many) Review
User (1) ----< (1) Cart
Product (1) ----< (Many) OrderItem
Product (1) ----< (Many) Review
Product (1) ----< (Many) CartItem
Category (1) ----< (Many) Product
Order (1) ----< (Many) OrderItem
Order (1) ----< (1) Payment


---

## 🧪 Testing

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test
```bash
mvn test -Dtest=ProductServiceImplTest
```

### Test Coverage
- Service layer: 15+ tests
- DTO validation: 4+ tests
- Total: 19+ tests with 100% pass rate

### Manual Testing with Postman

1. Import Postman collection (if available)
2. Or create requests manually:
    - Login to get JWT token
    - Use token in Authorization header: `Bearer {token}`
    - Test all endpoints

---

## 🚀 Deployment

### Build JAR
```bash
mvn clean package
java -jar target/e-commerce-0.0.1-SNAPSHOT.jar
```

### Deploy to Cloud

**Option 1: Render.com** (Recommended for free tier)
```bash
# Install Render CLI
# Connect Git repository
# Deploy with GitHub integration
```

**Option 2: Railway.app**
```bash
# Sign up with GitHub
# Connect repository
# Auto-deploy on push
```

**Option 3: Heroku Alternative (Fly.io)**
```bash
# Install Fly CLI
# Deploy: flyctl deploy
```

### Environment Variables (Production)
```env
SPRING_DATASOURCE_URL=jdbc:mysql://host:port/database
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET=your_long_secret_key
JWT_EXPIRATION=3600000
```

---

## 📈 Performance & Scalability

- Pagination and sorting for product listings
- Database queries implemented using Spring Data JPA
- Transaction management for order placement and cancellation
- Designed with a layered architecture for maintainability and scalability

---

## 🔐 Security Considerations

✅ JWT tokens for stateless authentication  
✅ BCrypt password hashing  
✅ Role-based access control  
✅ SQL injection prevention (JPA)  
✅ CORS configuration  
✅ Input validation on all endpoints  
✅ No sensitive data in logs  
✅ HTTPS ready for production

---

## 📝 Learning Outcomes

This project demonstrates expertise in:

- **Full-Stack REST API Development**
- **Enterprise Spring Boot Architecture**
- **Database Design & Relationships**
- **Transaction Management**
- **Security & Authentication**
- **API Documentation**
- **Unit Testing**
- **Git Version Control**
- **Agile Development Practices**

---

## 🤝 Contributing

This is a portfolio project. Fork and customize for your own use.

---

## 📧 Author

**Lalit Singh**
- Email: lalitsingh7122002@gmail.com
- GitHub: [@lalit_singh](https://github.com/lalitsingh712)
- LinkedIn: [Lalit Singh](https://linkedin.com/in/lalitsingh7122002)
- Instagram: [@code_with_lalit](https://instagram.com/code_with_lalit)

---

## 📄 License

This project is open source and available under the MIT License.

---

## 🙋 Support

For questions or issues:
1. Check existing GitHub issues
2. Create a new issue with details
3. Include error logs and steps to reproduce

---

## 🎓 Resources & References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Swagger/OpenAPI](https://springdoc.org/)
- [JUnit 5 Testing](https://junit.org/junit5/)
- [JWT Authentication](https://jwt.io/)

---

**Made with ❤️ using Spring Boot | Last Updated: Aug 2026**
