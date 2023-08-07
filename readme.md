# Github repo ground Rules

1. Merging changes to sub-main
   
```text
Please merge your latest branches to the sub-main branch(trunk/development) and leave the main branch untouched. We will merge sub-main with main once sub-main is stable and runnable.

This is to ensure that the main branch will always be a stable fallback in the event that there are problems with sub-main.
```

2. Branch naming convention

```
Suggested naming convention for branches: featureName-yourName, where featureName is the name of the feature you're working on in that branch and yourName is your name.

For example, if James is working on the manager controller, he can name his branch, "manager-controller-james". This would make it easier for us to know what each branch is for and who it belongs to. Thereafter, if James's teammate, Jill, would like to make changes to James's code, she can create a new branch from his branch and call it "manager-controller-jill".

If you're making branches to test stuff out, you can also name it using test-yourName, e.g. "test-james".

```

3. Each member must commit own code to feature branch daily (@1800) and must merge own developed feature branch into trunk/development branch before the due date.(you must fix the conflict which caused during merging).

4. Each developer should make your code readable with short/simple comment 

## repo tree

```text
└── AD_Repo/
    ├── main
    ├── dev
    ├── feature1-cyrus
    └── feature2-bert

```
