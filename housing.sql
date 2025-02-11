-- Create database for housing service
CREATE DATABASE IF NOT EXISTS housing_service;
USE housing_service;

-- Create Landlord table
CREATE TABLE Landlord (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    CellPhone VARCHAR(15) NOT NULL
);

-- Create House table
CREATE TABLE House (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    LandlordID INT NOT NULL,
    Address VARCHAR(255) NOT NULL,
    MaxOccupant INT NOT NULL,
    CONSTRAINT fk_house_landlord FOREIGN KEY (LandlordID) 
        REFERENCES Landlord(ID)
);

-- Create Facility table
CREATE TABLE Facility (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    HouseID INT NOT NULL,
    Type VARCHAR(50) NOT NULL,
    Description VARCHAR(255),
    Quantity INT NOT NULL,
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModificationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_facility_house FOREIGN KEY (HouseID) 
        REFERENCES House(ID)
);

-- Create FacilityReport table
CREATE TABLE FacilityReport (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    FacilityID INT NOT NULL,
    EmployeeID INT NOT NULL,
    Title VARCHAR(100) NOT NULL,
    Description TEXT NOT NULL,
    Status ENUM('Open', 'In Progress', 'Closed') NOT NULL DEFAULT 'Open',
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_facilityreport_facility FOREIGN KEY (FacilityID) 
        REFERENCES Facility(ID)
);

-- Create FacilityReportDetail table
CREATE TABLE FacilityReportDetail (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    FacilityReportID INT NOT NULL,
    EmployeeID INT NOT NULL,
    Comment TEXT NOT NULL,
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModificationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_facilityreportdetail_report FOREIGN KEY (FacilityReportID) 
        REFERENCES FacilityReport(ID)
);
