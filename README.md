# Text Editor FROM SCRATCH 
[taken from](https://austinhenley.com/blog/challengingprojects.html)

## Key Features

### **Rope Data Structure for Efficient Text Manipulation**
- Implements the **Rope class**, which is a binary tree used for storing and manipulating large text. The Rope structure allows for:
    - **Fast Insertion and Deletion**: Operations that traditionally take O(n) time in an array-based text editor are optimized to O(log n), making it suitable for handling larger files without performance degradation.
    - **Efficient Modification**: The Rope class supports efficient modification of text fragments, ensuring minimal overhead and fast response times.
    - **Optimized for Large Text**: The structure minimizes memory overhead by breaking down text into smaller chunks, improving the performance of complex operations like inserting, deleting, or modifying long paragraphs.

### **Undo/Redo Functionality with the Command Pattern**
- Uses the **Command Pattern** to implement flexible and extensible undo/redo functionality.
    - **Command Objects**: Each user action (e.g., typing, deleting, formatting) is encapsulated in a command object, allowing it to be executed or undone easily.
    - **State Management**: The undo/redo system efficiently tracks the state of the text, allowing users to revert or repeat actions while maintaining the integrity of the document.

### **JavaFX-Based Interactive User Interface**
- Built using **JavaFX**, the editor's user interface is designed to be simple and nothing much.

### **Basic Text Editing Capabilities**
- Supports standard text editor features, including:
    - **Open and Save**: Easily open existing documents and save your work with automatic file handling.
    - **New Document Creation**: Start with a blank document to create new files from scratch.
    - **Text Editing**: Basic features like typing, deleting, and formatting text are available to the user.

## Additional Functionalities Currently Under Implementation
- **Search and Replace**
- **More File Saving Formats**
- **Multiple File Handling**
