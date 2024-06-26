# Unbound Drag & Drop

[![Maven Central Version](https://img.shields.io/maven-central/v/dev.adriankuta/unbound-drag-drop?style=plastic)](https://central.sonatype.com/artifact/dev.adriankuta/unbound-drag-drop)
[![License: MIT](https://img.shields.io/github/license/AdrianKuta/Unbound-Drag-Drop?style=plastic)](https://github.com/AdrianKuta/Unbound-Drag-Drop/blob/master/LICENSE)

[Unbound Drag & Drop.webm](https://github.com/AdrianKuta/Unbound-Drag-Drop/assets/46381935/73cc7ac8-aaf0-4dd7-b506-0745b1122222)

Unbound Drag & Drop is a versatile and user-friendly feature designed to enhance your Android
applications by enabling drag and drop functionality across multiple RecyclerViews. Unlike the
default behavior, which restricts drag and drop actions within a single RecyclerView, Unbound Drag &
Drop allows users to seamlessly move items between different RecyclerViews, providing a more
flexible and intuitive user experience.

## Features

- **Multi-RecyclerView Drag and Drop**: Easily drag and drop items between multiple RecyclerViews.
- **Customizable and Extensible**: Adapt the feature to fit your specific needs with customizable
  options and extensible components.
- **Smooth and Intuitive User Experience**: Enhance the interactivity of your app with a
  user-friendly drag and drop interface.
- **Easy Integration**: Quickly integrate Unbound Drag & Drop into your existing Android projects
  with minimal setup.

## Installation

To include Unbound Drag & Drop in your project, add the following dependency to your build.gradle
file:

```kotlin
implementation("dev.adriankuta:unbound-drag-drop:0.0.2")
```

## Usage

### Setup

#### Step 1: Implement the Callback

Create a class that extends `DragDropHelper.Callback` to handle drag and drop events.

```kotlin
class MyDragDropCallback : DragDropHelper.Callback() {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        targetRecyclerView: RecyclerView,
        targetViewHolder: RecyclerView.ViewHolder?
    ): Boolean {
        // Handle the move logic here
        return true
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        targetRecyclerView: RecyclerView,
        targetViewHolder: RecyclerView.ViewHolder?
    ) {
        // Handle post-move logic here
    }
}
```

#### Step 2: Attach `DragDropHelper` to `RecyclerView`

In your Activity or Fragment, attach DragDropHelper to your RecyclerView.

```kotlin
val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
val dragDropHelper = DragDropHelper(MyDragDropCallback())
dragDropHelper.attachToRecyclerView(recyclerView)
```

#
### Step 3: Enable long click

Dragging is enabled only for views with long click enabled. To allow dragging and dropping of RecyclerView items, you need to set isLongClickable to true for each RecyclerView item.

```kotlin
viewHolder.itemView.isLongClickable = true

```

---

See example in `app` module.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss any changes.
