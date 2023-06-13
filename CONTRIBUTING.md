# How to contribute 
- Sign up via the GitHub account at https://github.com/

## Instructions for submitting a pull request👩‍💻👨‍💻:

Below you will find the process and workflow used to review and merge your changes.
- If you don't have git on your machine, [install it](https://help.github.com/articles/set-up-git/).

## Step 1 : Find an issue
- Take a look at the Existing issues or create your **own** issues!
- To discover new issues, you have two options. Firstly, you can download the "Taaveez app" from the Play Store using the provided [Link](https://play.google.com/store/apps/details?id=com.itssuryansh.taaveez). Alternatively, you can fork the code from Step 2 and run the app on an emulator.
- Wait for the issue to be assigned to you after which you can start working on it.

## Step 2 : Fork the Project
- Fork this Repository. This will create a Local Copy of this Repository on your Github Profile. Keep a reference to the original project in `upstream` remote.

```bash
$ git clone https://github.com/Suryansh1720001/Taaveez
$ cd Taaveez
$ git remote add upstream https://github.com/Suryansh1720001/Taaveez
```

- If you have already forked the project, update your copy before working.

```bash
$ git remote update
$ git checkout <branch-name>
$ git rebase upstream/<branch-name>
```
## Step 3 : Branch

```bash
# To create a new branch with name branch_name and switch to that branch 
$ git checkout -b <branch_name>
```
## Step 4 : Work on the issue assigned
- Work on the issue assigned to you. 
- After you've made changes or made your contribution to the project add changes to the branch you've just created by:

```bash
# To add all new files to branch branch_name
$ git add .
```
## Step 5 : Commit

- To commit give a descriptive message for the convenience of reveiwer by:

```bash
# This message get associated with all files you have changed
$ git commit -m "message"
```

## Step 6 : Work Remotely
- Now you are ready to your work to the remote repository.
- When your work is ready and complies with the project conventions, upload your changes to your fork:

```bash
# To push your work to your remote repository
$ git push -u origin <branch_name>
```

## Step 7 : Pull Request
- Go to your repository in browser and click on compare and pull requests. Then add a title and description to your pull request that explains your contribution.
- Then add a title to your pull request.
- Mention the issue number in your PR.
- Give a brief description about the changes.
- Your Pull Request has been submitted and will be reviewed by the owner and merged.🥳

## Contributing as a designer:
- All of the above
- Create a PR under the folder "designs" that will be available at the root of this repository
- Wait for your PR to get accepted
