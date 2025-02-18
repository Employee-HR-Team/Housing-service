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
USE hr_portal;

-- insert houses using existing landlord IDs
INSERT INTO House (LandlordID, Address, MaxOccupant) VALUES
(4, '789 Summer Street, Boston, MA 02110', 5),  -- Michael Wilson
(4, '42 Winter Lane, Boston, MA 02111', 3),     -- Michael Wilson
(5, '156 Beacon Hill, Boston, MA 02108', 4),    -- Sarah Anderson
(6, '23 Harvard Ave, Cambridge, MA 02138', 6),  -- David Thompson
(7, '88 Tech Square, Cambridge, MA 02139', 4);  -- Emily Taylor

-- Get the IDs of the newly inserted houses
SET @house1_id = (SELECT ID FROM House WHERE Address LIKE '%Summer Street%');
SET @house2_id = (SELECT ID FROM House WHERE Address LIKE '%Winter Lane%');
SET @house3_id = (SELECT ID FROM House WHERE Address LIKE '%Beacon Hill%');
SET @house4_id = (SELECT ID FROM House WHERE Address LIKE '%Harvard Ave%');
SET @house5_id = (SELECT ID FROM House WHERE Address LIKE '%Tech Square%');

-- Insert facilities with correct house IDs
INSERT INTO Facility (HouseID, Type, Description, Quantity) VALUES
-- For first house (Summer Street)
(@house1_id, 'Bed', 'Queen size beds', 5),
(@house1_id, 'Desk', 'Study desks', 3),
(@house1_id, 'Chair', 'Office chairs', 5),
(@house1_id, 'Mattress', 'Memory foam mattresses', 5),

-- For second house (Winter Lane)
(@house2_id, 'Bed', 'Twin XL beds', 3),
(@house2_id, 'Desk', 'Computer desks', 3),
(@house2_id, 'Chair', 'Ergonomic chairs', 3),

-- For Beacon Hill house
(@house3_id, 'Bed', 'King size beds', 4),
(@house3_id, 'Sofa', 'Leather sofa', 2),
(@house3_id, 'Table', 'Dining table', 1),

-- For Harvard Ave house
(@house4_id, 'Bed', 'Full size beds', 6),
(@house4_id, 'Desk', 'Standing desks', 6),
(@house4_id, 'Chair', 'Gaming chairs', 6);

-- Get facility IDs for reports
SET @facility1_id = (SELECT ID FROM Facility WHERE Description = 'Standing desks' LIMIT 1);
SET @facility2_id = (SELECT ID FROM Facility WHERE Description = 'Gaming chairs' LIMIT 1);
SET @facility3_id = (SELECT ID FROM Facility WHERE Description = 'Leather sofa' LIMIT 1);
SET @facility4_id = (SELECT ID FROM Facility WHERE Description = 'Memory foam mattresses' LIMIT 1);

-- Insert facility reports with correct facility IDs
INSERT INTO FacilityReport (FacilityID, EmployeeID, Title, Description, Status) VALUES
(@facility1_id, 301, 'Standing Desk Wobble', 'Desk is unstable when in standing position', 'Open'),
(@facility2_id, 302, 'Chair Adjustment Broken', 'Height adjustment mechanism not working', 'InProgress'),
(@facility3_id, 303, 'Sofa Stain', 'Large coffee stain on leather sofa', 'Open'),
(@facility4_id, 304, 'Mattress Replacement', 'Springs are coming through', 'Closed');

-- Get report IDs for comments
SET @report1_id = (SELECT ID FROM FacilityReport WHERE Title = 'Standing Desk Wobble');
SET @report2_id = (SELECT ID FROM FacilityReport WHERE Title = 'Chair Adjustment Broken');
SET @report3_id = (SELECT ID FROM FacilityReport WHERE Title = 'Sofa Stain');
SET @report4_id = (SELECT ID FROM FacilityReport WHERE Title = 'Mattress Replacement');

-- Insert report comments with correct report IDs
INSERT INTO FacilityReportDetail (FacilityReportID, EmployeeID, Comment) VALUES
-- Standing Desk Report
(@report1_id, 301, 'Initial inspection: desk wobbles when raised above 40 inches'),
(@report1_id, 401, 'Scheduled maintenance for Friday'),
(@report1_id, 301, 'Please expedite, affecting work productivity'),

-- Chair Report
(@report2_id, 302, 'Chair cannot maintain height, drops slowly'),
(@report2_id, 402, 'Ordered replacement pneumatic cylinder'),
(@report2_id, 402, 'Parts arrived, scheduling repair'),

-- Sofa Report
(@report3_id, 303, 'Professional cleaning attempt unsuccessful'),
(@report3_id, 403, 'Getting quotes for reupholstery'),

-- Mattress Report
(@report4_id, 304, 'Mattress determined beyond repair'),
(@report4_id, 404, 'New mattress ordered'),
(@report4_id, 404, 'Replacement completed'),
(@report4_id, 304, 'Confirming new mattress received and installed');
