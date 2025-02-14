Housing Service

<img width="473" alt="housing service" src="https://github.com/user-attachments/assets/102d6324-7544-4743-aae5-7c8ebbe7cdcf" />


Housing Service Microservice

This microservice is part of the Employee Onboarding System, responsible for managing employee housing arrangements, facilities, and facility reports.

Features

House Management
- View all houses with summary information
- Get detailed house information by ID
- Add new houses with landlord information and facilities
- Update existing house details
- Delete houses

Facility Management
- View facilities for each house
- Update facility quantities (beds, mattresses, tables, chairs)
- Track facility information including creation and modification dates

Facility Report Management
- Create and track facility issue reports
- Support comment threads on facility reports
- Track report status (Open, InProgress, Closed)
- View reports by house, facility, status, or employee

API Endpoints

House Management
- `GET /api/houses` - Get all houses summary
- `GET /api/houses/{id}` - Get house details by ID
- `POST /api/houses` - Add a new house
- `PUT /api/houses/{id}` - Update house details
- `DELETE /api/houses/{id}` - Delete a house

Facility Management
- `GET /api/houses/{houseId}/facilities` - Get facility information for a house
- `PUT /api/houses/{houseId}/facilities` - Update facility information

Facility Report Management
- `GET /api/facility-reports` - Get all facility reports
- `GET /api/facility-reports/{reportId}` - Get facility report by ID
- `POST /api/facility-reports` - Create a new facility report
- `PUT /api/facility-reports/{reportId}/status` - Update report status
- `POST /api/facility-reports/{reportId}/comments` - Add a comment to a report
- `GET /api/facility-reports/status/{status}` - Get reports by status
- `GET /api/facility-reports/employee/{employeeId}` - Get reports by employee
- `DELETE /api/facility-reports/{reportId}` - Delete a facility report

Database Schema

The service uses MySQL with the following tables:
- `House` - Store house information
- `Landlord` - Store landlord information
- `Facility` - Store facility information for each house
- `FacilityReport` - Store facility issue reports
- `FacilityReportDetail` - Store comments on facility reports
