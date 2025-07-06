# Aelpyeong (Korean Hip-hop Album Review Platform)

## Introduction
This project is a web platform designed for Korean hip-hop enthusiasts to share reviews and ratings of hip-hop albums. It provides a community space where users can write album reviews and exchange opinions with other users.

### Key Features
- Album review creation and sharing
- User authentication and authorization
- Image upload and thumbnail generation
- Community and comment system
- Email notification service

## Tech Stack

### Backend
| Category | Technology | Version |
|----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.3.5 |
| Database | MySQL | 8.0.33 |
| ORM | Spring Data JPA | 3.1.5 |
| Security | Spring Security | 6.1.5 |

### Frontend & Template
| Category | Technology | Version |
|----------|------------|---------|
| Template Engine | Thymeleaf | 3.1.2 |
| Layout | Thymeleaf Layout Dialect | 3.1.0 |

### Infrastructure & Tools
| Category | Technology | Version |
|----------|------------|---------|
| Storage | AWS S3 | 2.2.6.RELEASE |
| Migration | Flyway | 9.16.3 |
| Documentation | Springfox | 3.0.0 |
| Build | Gradle | 8.4 |
| Utilities | Lombok | 1.18.30 |
| Mapping | ModelMapper | 3.1.0 |

## Data Migration

### Migration Script Structure
```sql
V2_insert_test_artists_and_albums_data.sql    # Automatically migrates test data for following models:
                                             # - Artists
                                             # - Albums
```

### Running Migrations
```bash
# Check Flyway configuration and migration status
./gradlew flywayInfo

# Run migration - This will automatically:
# 1. Create all necessary database tables
# 2. Insert initial test data for all models
# 3. Set up required relationships between models
./gradlew flywayMigrate

# Reset migration (Warning: Deletes all data)
./gradlew flywayClean
```

## System Architecture
- Based on Spring MVC architecture
- Image storage system using AWS S3
- Authentication/Authorization handling through Spring Security
- Database migration management using Flyway
- Server-side rendering with Thymeleaf

## Installation & Running

### Prerequisites
- JDK 17 or higher
- MySQL 8.0 or higher
- AWS Account (for S3 usage)

### Configuration
1. Create MySQL database
2. Configure `application.properties` file
   ```properties
   # Database
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   # AWS
   cloud.aws.credentials.access-key=your_access_key
   cloud.aws.credentials.secret-key=your_secret_key
   cloud.aws.s3.bucket=your_bucket_name
   ```

### Running the Application
```bash
./gradlew bootRun
```

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── io/
│   │       └── github/
│   │           └── haebin827/
│   │               ├── controller/    # MVC Controllers
│   │               ├── service/       # Business Logic
│   │               ├── repository/    # Data Access Layer
│   │               ├── domain/        # Domain Models
│   │               └── config/        # Configuration Classes
│   └── resources/
│       ├── static/                    # Static Resources
│       ├── templates/                 # Thymeleaf Templates
│       └── db/migration/             # Flyway Migration Scripts
```

## UX/UI Documentation (Updated on 07/04/2025)

### 1. Main Page (Landing Page)
![Main Page](src/main/resources/static/assets/ui/mainPage.png)

### 2. Authentication & Registration
#### Login Related Pages
- **Login Page**  
  ![Login Page](src/main/resources/static/assets/ui/loginPage.png)
- **Registration Page**  
  ![Registration Page](src/main/resources/static/assets/ui/registerPage.png)
  - With Validation:  
    ![Registration with Validation](src/main/resources/static/assets/ui/registerPage_validation.png)
  - Registration Complete:  
    ![Registration Complete](src/main/resources/static/assets/ui/finishedRegisterPage.png)
  - Email Verification:  
    ![Email Verification](src/main/resources/static/assets/ui/registerEmailVerificationPage.png)
- **ID/Password Recovery**  
  ![Find ID/Password](src/main/resources/static/assets/ui/findIdPwPage.png)
  - Email Verification:  
    ![Email Verification for Recovery](src/main/resources/static/assets/ui/findIdPwPage_email_verify.png)

### 3. Albums Section
#### Album Listing
![Albums Page](src/main/resources/static/assets/ui/albumsPage.png)
![Albums Page Alternative View](src/main/resources/static/assets/ui/albumsPage2.png)
> Note: "Register Artist" and "Register Album" buttons are only visible to administrators

#### Album Details
- **Without Reviews**  
  ![Album Details](src/main/resources/static/assets/ui/albumsPage_read.png)
  ![Album Details Extended](src/main/resources/static/assets/ui/albumsPage_read2.png)
- **With Reviews**  
  ![Album with Reviews](src/main/resources/static/assets/ui/albumsPage_review3.png)
  ![Album with Reviews Extended](src/main/resources/static/assets/ui/albumsPage_review4.png)

#### Review Management
- **Write Review Page**  
  ![Write Review](src/main/resources/static/assets/ui/reviewPage.png)
- **Album Registration** (Admin Only)  
  ![Upload New Album](src/main/resources/static/assets/ui/uploadNewAlbumPage_admin.png)

### 4. Artists Section
#### Artist Listing
![Artists Page](src/main/resources/static/assets/ui/artistsPage.png)
![Artists Page Alternative View](src/main/resources/static/assets/ui/artistsPage2.png)
> Note: "Register Artist" and "Register Album" buttons are only visible to administrators

#### Artist Management
- **Artist Registration** (Admin Only)  
  ![Upload New Artist](src/main/resources/static/assets/ui/uploadNewArtistPage_admin.png)

### 5. Announcements Section
#### Announcement Listing
![Announcements Page](src/main/resources/static/assets/ui/announcementPage.png)
> Note: "Write" button is only visible to administrators

#### Announcement Management
- **View Announcement**  
  ![Read Announcement](src/main/resources/static/assets/ui/announcementPage_read.png)
- **Edit Announcement** (Admin Only)  
  ![Edit Announcement](src/main/resources/static/assets/ui/accouncementPage_edit.png)

### 6. Support Section
#### Information Pages
- **About Page**  
  ![About Page](src/main/resources/static/assets/ui/aboutPage.png)
- **Artist Request** (Logged-in Users Only)  
  ![Artist Request](src/main/resources/static/assets/ui/artistRequestPage_user.png)
- **Feedback** (Logged-in Users Only)  
  ![Feedback Page](src/main/resources/static/assets/ui/feedbackPage_user.png)
- **FAQ**  
  ![FAQ Page](src/main/resources/static/assets/ui/faqPage.png)
- **Terms of Service**  
  ![Terms Page](src/main/resources/static/assets/ui/termsPage.png)
- **Development Log**  
  ![Development Log](src/main/resources/static/assets/ui/devlogPage.png)
  ![Development Log Extended](src/main/resources/static/assets/ui/devlogPage2.png)

### 7. User Profile
#### Profile Pages (Logged-in Users Only)
![Profile Page](src/main/resources/static/assets/ui/profilePage.png)
![Profile Page Extended](src/main/resources/static/assets/ui/profilePage2.png)