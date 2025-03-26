# Horus - Recruitment Task

This document analyzes the provided code, explaining its components, relationships, and interpretation. Additionally, it outlines the implementation of required methods and the rationale behind them.

## Analysis
Assuming the provided code prohibits changes to interfaces (modifying or adding methods or altering method signatures), we can only implement the following methods within `FileCabinet`:

- `findFolderByName`
- `findFoldersBySize`
- `count`

Additionally, helper methods can be added inside `FileCabinet` to aid these implementations.

### Additional Components
An **enum** should be used for validating folder size (which is currently a string representation):
- SMALL
- MEDIUM
- LARGE

## Code Structure
### Provided Code Components:
- **Interfaces**:
  - `Folder`
  - `Cabinet`
  - `MultiCabinet`
- **Classes**:
  - `FileCabinet`
  - `MultiFolderProvider`
  - `DuplicatedFolderException`
  - `FileSize`
- **Enum**:
  - `FileSize`

### Relationships
- `MultiFolder` extends `Folder`, making it the base type for the cabinet's folder collection.
- `Folder` holds minimal details such as name and size.
- `MultiFolder` adds a list of subfolders.
- `FileCabinet` depends on `MultiCabinet` for retrieving subfolders.

### Concept
Each folder is represented by the `Folder` interface. Groups of folders use `MultiFolder`, which extends `Folder`.

Example folder structure:
```
.
├── movies
│   ├── polish
│   └── world
└── games
    └── rpg
        ├── polish
        └── world
```

Folder representation:
- **MultiFolders**:
  - movies
  - games
  - rpg
- **Single Folders**:
  - movies/polish
  - movies/world
  - games/rpg/polish
  - games/rpg/world

If `games` is passed as a `MultiFolder`, `count()` should return **4**.

## Implementation
### Tech Stack:
- Maven
- Java 23
- JUnit for unit testing

### Design patterns


### Implementation Details
`FileCabinet` implements required methods from `Cabinet`:
- `Optional<Folder> findFolderByName(String name)`
- `List<Folder> findFoldersBySize(String size)`
- `int count()`

### FileCabinet Class
#### Purpose:
Manages folders, including root and subfolders, with functionality for searching by name, filtering by size, and counting folders.

#### Attributes:
- `folders` (`List<Folder>`): Root folders managed by `FileCabinet`.
- `multiCabinet` (`MultiCabinet`): Service for retrieving subfolders.

#### Methods:
##### `findFolderByName(String name)`
Finds a folder by name across all folders (root + subfolders).
- **Returns**: `Optional<Folder>` (throws `DuplicatedFolderException` if multiple matches exist).
- **Logic**:
  - Retrieve all folders using `getAllFolders()`.
  - Filter by name.
  - Handle duplicate folder names with an exception.

##### `findFoldersBySize(String size)`
Finds folders by their size.
- **Returns**: `List<Folder>`.
- **Validation**: Uses `FileSize.validateSize()`.
- **Logic**:
  - Validate size.
  - Retrieve all folders.
  - Filter by size.

##### `count()`
Returns the total number of folders.
- **Returns**: `int` (total count of root + subfolders).
- **Logic**:
  - Collect all folders using `getAllFolders()`.
  - Return the size of the list.

##### `getAllFolders(List<Folder> rootFolders)`
Retrieves all folders recursively.
- **Returns**: `List<Folder>` (root + subfolders).
- **Logic**:
  - Iterate through `rootFolders`.
  - Use `multiCabinet.getSubFolder(folder)` for subfolders. Also, to count every subfolder recursive way has been used.

### Problems
Although the use of instanceof is generally discouraged in OOP, it is acceptable in this context due to the constraints, as no modifications can be made to the provided code. Therefore, instanceof is the chosen approach for handling folder types.

## Summary
The implementation follows SOLID principles while adhering to the given constraints. Some design choices are suboptimal due to the requirement that all logic remains inside `FileCabinet`. However, since the project is small and purely Java-based (without frameworks), this approach is acceptable.

