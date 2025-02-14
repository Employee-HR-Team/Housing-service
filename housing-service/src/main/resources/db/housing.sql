-- Create database for housing service
CREATE DATABASE IF NOT EXISTS housing_service;
USE housing_service;

-- Create Landlord table
CREATE TABLE Landlord (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL UNIQUE,
    CellPhone VARCHAR(15) NOT NULL,
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModificationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create House table
CREATE TABLE House (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    LandlordID INT NOT NULL,
    Address VARCHAR(255) NOT NULL,
    MaxOccupant INT NOT NULL,
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModificationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_house_landlord FOREIGN KEY (LandlordID) REFERENCES Landlord(ID)
);

-- Create indexes for House table
CREATE INDEX idx_house_landlord ON House(LandlordID);

-- Create Facility table
CREATE TABLE Facility (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    HouseID INT NOT NULL,
    Type VARCHAR(50) NOT NULL,
    Description VARCHAR(255),
    Quantity INT NOT NULL,
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModificationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_facility_house FOREIGN KEY (HouseID) REFERENCES House(ID)
);

-- Create indexes for Facility table
CREATE INDEX idx_facility_house ON Facility(HouseID);
CREATE INDEX idx_facility_type ON Facility(Type);

-- Create FacilityReport table
CREATE TABLE FacilityReport (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    FacilityID INT NOT NULL,
    EmployeeID INT NOT NULL,
    Title VARCHAR(100) NOT NULL,
    Description TEXT NOT NULL,
    Status ENUM('Open', 'InProgress', 'Closed') NOT NULL DEFAULT 'Open',
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModificationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_facilityreport_facility FOREIGN KEY (FacilityID) REFERENCES Facility(ID)
);

-- Create indexes for FacilityReport table
CREATE INDEX idx_report_facility ON FacilityReport(FacilityID);
CREATE INDEX idx_report_employee ON FacilityReport(EmployeeID);
CREATE INDEX idx_report_status ON FacilityReport(Status);

-- Create FacilityReportDetail table
CREATE TABLE FacilityReportDetail (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    FacilityReportID INT NOT NULL,
    EmployeeID INT NOT NULL,
    Comment TEXT NOT NULL,
    CreateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    LastModificationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_facilityreportdetail_report FOREIGN KEY (FacilityReportID) REFERENCES FacilityReport(ID)
);

-- Create indexes for FacilityReportDetail table
CREATE INDEX idx_reportdetail_report ON FacilityReportDetail(FacilityReportID);
CREATE INDEX idx_reportdetail_employee ON FacilityReportDetail(EmployeeID);

-- Insert test data
-- Insert test landlords
INSERT INTO Landlord (FirstName, LastName, Email, CellPhone) VALUES
('John', 'Doe', 'john.doe@email.com', '123-456-7890'),
('Jane', 'Smith', 'jane.smith@email.com', '234-567-8901'),
('Robert', 'Johnson', 'robert.j@email.com', '345-678-9012');

-- Insert test houses
INSERT INTO House (LandlordID, Address, MaxOccupant) VALUES
(1, '123 Main St, Boston, MA 02115', 4),
(1, '456 Park Ave, Boston, MA 02116', 3),
(2, '789 Washington St, Boston, MA 02118', 5),
(3, '321 Beacon St, Boston, MA 02119', 4);

-- Insert test facilities
INSERT INTO Facility (HouseID, Type, Description, Quantity) VALUES
-- For first house
(1, 'Bed', 'Queen size beds', 4),
(1, 'Table', 'Dining table', 1),
(1, 'Chair', 'Dining chairs', 4),
-- For second house
(2, 'Bed', 'Twin size beds', 3),
(2, 'Table', 'Study tables', 3),
(2, 'Chair', 'Study chairs', 3),
-- For third house
(3, 'Bed', 'Full size beds', 5),
(3, 'Table', 'Kitchen table', 1),
(3, 'Chair', 'Kitchen chairs', 6);

-- Insert test facility reports
INSERT INTO FacilityReport (FacilityID, EmployeeID, Title, Description, Status) VALUES
(1, 1, 'Broken Bed Frame', 'The bed frame in room 1 is broken', 'Open'),
(2, 2, 'Table Wobbling', 'Dining table is unstable', 'InProgress'),
(4, 1, 'Bed Squeaking', 'Bed makes noise when moving', 'Closed'),
(7, 3, 'Mattress Issues', 'Mattress needs replacement', 'Open');

-- Insert test facility report details (comments)
INSERT INTO FacilityReportDetail (FacilityReportID, EmployeeID, Comment) VALUES
(1, 1, 'Initial report: bed frame cracked on left side'),
(1, 3, 'Maintenance scheduled for next week'),
(2, 2, 'Table legs need tightening'),
(2, 3, 'Maintenance team is working on it'),
(3, 1, 'Squeaking noise from springs'),
(3, 3, 'Fixed by tightening all bolts'),
(4, 3, 'Will inspect the mattress condition');

-- Create helpful views for common queries
CREATE VIEW vw_house_summary AS
SELECT 
    h.ID as HouseID,
    h.Address,
    h.MaxOccupant,
    CONCAT(l.FirstName, ' ', l.LastName) as LandlordName,
    l.Email as LandlordEmail,
    l.CellPhone as LandlordPhone,
    COUNT(DISTINCT f.ID) as FacilityCount,
    COUNT(DISTINCT fr.ID) as OpenReportCount
FROM House h
JOIN Landlord l ON h.LandlordID = l.ID
LEFT JOIN Facility f ON h.ID = f.HouseID
LEFT JOIN FacilityReport fr ON f.ID = fr.FacilityID AND fr.Status = 'Open'
GROUP BY h.ID, h.Address, h.MaxOccupant, LandlordName, LandlordEmail, LandlordPhone;

-- Create view for facility reports with comments
CREATE VIEW vw_facility_reports_with_comments AS
SELECT 
    fr.ID as ReportID,
    f.HouseID,
    fr.Title,
    fr.Description as ReportDescription,
    fr.Status,
    fr.CreateDate as ReportCreateDate,
    frd.ID as CommentID,
    frd.Comment,
    frd.EmployeeID as CommentByEmployeeID,
    frd.CreateDate as CommentCreateDate
FROM FacilityReport fr
JOIN Facility f ON fr.FacilityID = f.ID
LEFT JOIN FacilityReportDetail frd ON fr.ID = frd.FacilityReportID
ORDER BY fr.CreateDate DESC, frd.CreateDate ASC;