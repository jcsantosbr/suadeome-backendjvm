Feature: API Contract for Backend
  This feature describe all endpoint available in the system, the parameters expected and usecases

  Background: The system has already some people configured
    Given the following users already registered
      | User  | Contact of  |
      | Alice | Bob, Carl   |
      | Bob   | Alice, Carl |
      | Carl  | Bob         |
      | Doug  |             |
    And the following professionals already registered
      | ID | Name   | Service | Phone | Grade | Recommendations | Grade By Contact | Recommended By |
      | 1  | Paula  | Plumber | 111   | 8     | 10              | 10               | Bob, Carl      |
      | 2  | Daniel | Doctor  | 222   | 9     | 20              | 8                | Bob, Carl      |
      | 3  | Ivan   | IT      | 333   | 8     | 15              | 6                | Bob, Carl      |
      | 4  | Ingrid | IT      | 444   | 8     | 5               | 8                | Bob            |
      | 5  | Dino   | Driver  | 555   | 8     | 1               | 0                |                |


  Scenario: List all professionals which provide a specific service recommended by contacts
  The list is ordered by ranking. The ranking is the same as medal in olympic games
    Given the current user is "Alice"
    When the user requests "professionals/?service=it&byContacts=true"
    Then the response should be:
    """
      {
        [
          {
            type: "Professional",
            id: 4,
            name: "Ingrid",
            phone: "444",
            service_id: <service_id>,
            recommendations: 2,
            grade: 8
          },
          {
            type: "Professional",
            id: 3,
            name: "Ivan",
            phone: "333",
            service_id: <service_id>,
            recommendations: 1,
            grade: 6
          }
        ]
      }
    """

  Scenario: List all professionals which provide a specific service recommended by anyone
  The list is ordered by ranking. The ranking is the same as medal in olympic games
    Given the current user is "Alice"
    When the user requests "/professionals/?service=it&byContacts=false"
    Then the response should be:
    """
      {
        [
          {
            type: "Professional",
            id: 3,
            name: "Ivan",
            phone: "333",
            service_id: <service_id>,
            recommendations: 15,
            grade: 8
          },
          {
            type: "Professional",
            id: 4,
            name: "Ingrid",
            phone: "444",
            service_id: <service_id>,
            recommendations: 5,
            grade: 8
          }
        ]
      }
    """

  Scenario: List all services recommended in the system
    Given the current user is "Alice"
    When the user requests "/services"
    Then the response should be:
    """
      [
        {
          type: "Service",
          id: <service_id>,
          normalized_name: "doctor",
          original_name: "Doctor"
        },
        {
          type: "Service",
          id: <service_id>,
          normalized_name: "it",
          original_name: "IT"
        },
        {
          type: "Service",
          id: <service_id>,
          normalized_name: "plumber",
          original_name: "Plumber"
        }
      ]
    """

  Scenario: List all services recommended using a prefix
    Given the following services already registered
      | Service    |
      | Detective  |
      | Demolition |
    When the user requests "/services?prefix=de"
    Then the response should be:
    """
      [
        {
          type: "Service",
          id: <service_id>,
          normalized_name: "demolition",
          original_name: "Demolition"
        },
        {
          type: "Service",
          id: <service_id>,
          normalized_name: "detective",
          original_name: "Detective"
        }
      ]
    """

  Scenario: Recommending new professionals
    Given the current user is "Alice"
    When the user posts the following command "/professionals"
    """
    {
      "name": "Paul Irvin",
      "phone": "666 666",
      "service": "Private Investigator"
      "grade": 10
    }
    """
    Then the response should be:
    """
    {
      "type": "Professional",
      "id": <professional_id>,
      "name": "Paul Irvin",
      "phone": "666666",
      "service_id": <service_id>
      "grade": 10
    }
    """

  Scenario: Recommending existing professionals
    Given the current user is "Alice"
    When the user posts the following command "/recommendations"
    """
    {
      "professional_id": 5,
      "grade": 10
    }
    """
    Then the response should be:
    """
    {
      "type": "Recommendation",
      "professional_id": 1,
      "grade": 9
    }
    """







