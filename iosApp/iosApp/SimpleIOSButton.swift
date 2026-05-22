import SwiftUI
import ComposeApp

class IOSNativeViewFactory: NativeViewFactory {
    static var shared = IOSNativeViewFactory()
    let buttonState = ButtonState()

    func createButtonView(label: String, isLoading: Bool, onClick: @escaping () -> Void) -> UIViewController {
        print("🔴 createButtonView called — isLoading: \(isLoading)")

        // ✅ Sync update — no async, no race condition
        buttonState.isLoading = isLoading
        print("✅ buttonState.isLoading = \(buttonState.isLoading)")

        let view = SimpleIOSButton(label: label, state: buttonState, action: onClick)
        return UIHostingController(rootView: view)
    }
}

class ButtonState: ObservableObject {
    @Published var isLoading: Bool = false
}

struct SimpleIOSButton: View {
    var label: String
    @ObservedObject var state: ButtonState
    var action: () -> Void

    var body: some View {
        Button(action: {
            print("👆 Button tapped — isLoading: \(state.isLoading)")
            action()
        }) {
            if state.isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                    .tint(.black)
            } else {
                Text(label)
                    .font(.headline)
            }
        }
        .disabled(state.isLoading)
    }
}