We decide indexes itemID and name of items on its attribute, union of the item name and description.

We use these two indexes because it can make indexing faster without accessing database again than if we use only one index.
Since our goal is to find out the items that have target keyword in the union of its name, category and description attributes, we indexes on the text of their unions.



