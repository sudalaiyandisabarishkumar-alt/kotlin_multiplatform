// iosApp/iosAppUITests/AppUITests.swift
import XCTest

class AppUITests: XCTestCase {

    var app: XCUIApplication!

    override func setUp() {
        app = XCUIApplication()
        app.launch()
    }

    override func tearDown() {
        app.terminate()
    }

    func test_ios_text_isDisplayed() {
        let text = app.staticTexts
            .matching(NSPredicate(format: "label CONTAINS 'iOS'"))
            .firstMatch
        XCTAssertTrue(
            text.waitForExistence(timeout: 5),
            "Expected iOS text to be displayed on screen"
        )
    }
}
