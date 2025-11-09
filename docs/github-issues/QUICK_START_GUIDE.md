# Quick Start Guide: Creating GitHub Issues from TODOs

This guide provides step-by-step instructions for creating the 12 GitHub issues from the TODO documents.

## Prerequisites Check

Before you begin, verify you have:
- [ ] GitHub account with write access to `TheStackTraceWhisperer/october`
- [ ] GitHub CLI (`gh`) installed on your machine
- [ ] Authentication set up for GitHub CLI

## Installation (If Needed)

### Install GitHub CLI

**macOS:**
```bash
brew install gh
```

**Windows:**
```powershell
winget install --id GitHub.cli
```

**Linux (Debian/Ubuntu):**
```bash
sudo apt install gh
```

**Linux (Red Hat/Fedora):**
```bash
sudo dnf install gh
```

For other platforms, see: https://cli.github.com/

### Authenticate GitHub CLI

After installation, authenticate:
```bash
gh auth login
```

Follow the prompts to:
1. Select "GitHub.com"
2. Choose "HTTPS" protocol
3. Authenticate via web browser
4. Complete the authentication flow

Verify authentication:
```bash
gh auth status
```

You should see: "✓ Logged in to github.com as [your-username]"

## Method 1: Automated Creation (Fastest) ⚡

### Step 1: Navigate to Repository
```bash
cd /path/to/october
```

### Step 2: Make Script Executable (if needed)
```bash
chmod +x create-issues-from-todos.sh
```

### Step 3: Run the Script
```bash
./create-issues-from-todos.sh
```

### Expected Output
```
Creating GitHub issues for the October project...
Repository: TheStackTraceWhisperer/october

✓ GitHub CLI authenticated

Creating issues from integration-todos.md...
==============================================

Creating issue: Implement TilemapRenderSystem
✓ Issue created

Creating issue: Choose and Implement Tilemap Collision Strategy
✓ Issue created

[... continues for all 12 issues ...]

==============================================
Issue Creation Complete!
==============================================

Created issues from:
  - integration-todos.md (6 issues)
  - zone-tilemap-sequence.md (6 issues)

Total: 12 GitHub issues
```

### Step 4: Verify Issues Created
```bash
gh issue list --repo TheStackTraceWhisperer/october
```

You should see 12 new issues listed.

## Method 2: Manual Creation (Full Control) 🎯

If you prefer to create issues manually or want to customize them:

### Step 1: Navigate to Template Directory
```bash
cd .github/ISSUE_TEMPLATES_FROM_TODOS/
```

### Step 2: Read the Template README
```bash
cat README.md
```

This provides an overview of all 12 issues with their priorities and relationships.

### Step 3: Create Issues One by One

For each issue (01 through 12):

1. **Open the template file**
   ```bash
   cat 01-implement-tilemap-render-system.md
   ```

2. **Copy the content** (excluding the YAML frontmatter lines that start with `---`)

3. **Go to GitHub** in your browser:
   https://github.com/TheStackTraceWhisperer/october/issues/new

4. **Paste the content** into the issue description

5. **Add labels** from the frontmatter:
   - Look at the `labels:` line in the frontmatter
   - Add each label in the GitHub UI
   - Example: `enhancement`, `high-priority`, `rendering`

6. **Submit the issue**

7. **Repeat** for issues 02 through 12

### Tips for Manual Creation
- Create high-priority issues first (01, 02, 07, 12)
- Use the template README to understand dependencies
- Cross-reference related issues by number after creation

## Method 3: Selective Creation 🎛️

To create only specific issues:

### Step 1: Edit the Script
```bash
nano create-issues-from-todos.sh
# or use your preferred editor
```

### Step 2: Comment Out Unwanted Issues

Find the `create_issue` call you want to skip and comment it out:

```bash
# Comment out issue #5 (typed UI events)
# create_issue \
# "Consider Switching UI Events to Typed Event Classes" \
# "..." \
# "enhancement,low-priority,polish,ui"
```

### Step 3: Run Modified Script
```bash
./create-issues-from-todos.sh
```

## After Creating Issues

### 1. Review Issues
```bash
gh issue list --repo TheStackTraceWhisperer/october --limit 20
```

### 2. View Individual Issues
```bash
gh issue view <issue-number> --repo TheStackTraceWhisperer/october
```

### 3. Organize Issues

**Add to Milestones:**
```bash
gh issue edit <issue-number> --milestone "v1.0" --repo TheStackTraceWhisperer/october
```

**Assign to Team Members:**
```bash
gh issue edit <issue-number> --assignee @username --repo TheStackTraceWhisperer/october
```

**Add to Projects:**
```bash
# Via web UI: Issues → Select issue → Projects panel → Add to project
```

### 4. Update Issue Relationships

Add cross-references by editing issue descriptions:
- Issue #04 depends on #03
- Issue #11 depends on #01
- Issue #12 may depend on #02

Example:
```bash
gh issue edit 4 --body "$(gh issue view 4 --json body -q .body)

Depends on #3" --repo TheStackTraceWhisperer/october
```

## Troubleshooting

### "gh: command not found"
**Solution:** Install GitHub CLI (see Installation section above)

### "Not authenticated with GitHub CLI"
**Solution:** Run `gh auth login` and follow the prompts

### "permission denied: create-issues-from-todos.sh"
**Solution:** Make the script executable: `chmod +x create-issues-from-todos.sh`

### "HTTP 403: Resource not accessible"
**Solution:** Verify you have write access to the repository

### Script runs but creates duplicate issues
**Solution:** Issues can only be created once. Delete duplicates manually or via:
```bash
gh issue close <issue-number> --repo TheStackTraceWhisperer/october
```

## Verification

After creating issues, verify completeness:

```bash
# Count issues (should show 12 new issues)
gh issue list --repo TheStackTraceWhisperer/october --limit 100 | wc -l

# List all issues with labels
gh issue list --repo TheStackTraceWhisperer/october --label enhancement --limit 20

# List high-priority issues
gh issue list --repo TheStackTraceWhisperer/october --label high-priority
```

## Next Steps

1. ✅ Issues created
2. 📋 Review issue descriptions for accuracy
3. 🏷️ Verify labels are correct
4. 📅 Assign to milestones
5. 👥 Assign to team members
6. 📊 Add to project boards
7. 🔗 Cross-reference related issues
8. 🚀 Begin implementation!

## Additional Resources

- **Main Documentation:** `TODO_CONVERSION_SUMMARY.md`
- **Script Guide:** `GITHUB_ISSUES_README.md`
- **Template Index:** `.github/ISSUE_TEMPLATES_FROM_TODOS/README.md`
- **GitHub CLI Docs:** https://cli.github.com/manual/

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review the documentation files
3. Verify your GitHub CLI installation and authentication
4. Check repository permissions

---

**Ready to proceed?** Choose your method and create those issues! 🚀
