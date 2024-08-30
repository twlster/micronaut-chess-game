Feature: Chess Game Feature

  Scenario: W Win Game
    Given The application is running
    When a game is created
    Then the "w" makes a move
    And the "w" wins the game

  Scenario: B Win Game
    Given The application is running
    When a game is created
    Then the "b" makes a move
    And the "b" wins the game

  Scenario: Draw Game
    Given The application is running
    When a game is created
    Then the "w" makes a move
    And the "b" makes a move
    And the game is a draw

#  Scenario Outline: Win Game
#    Given The application is running
#    When game with name <gameName> and <winner> winner
#    Then the account is credited with 10.0
#    And account should have a balance of 10.0
#
#    Examples:
#      | gameName | winner |
#      | New Game | w      |