# GitHub Issues from TODO Documents

This directory contains documentation for converting TODO documents into GitHub issues.

## Quick Start

To create GitHub issues from TODO documents:

```bash
./create-issues-from-todos.sh
```

Requires GitHub CLI (`gh`) to be installed and authenticated.

## Documentation Files

- **QUICK_START_GUIDE.md** - Step-by-step installation and usage instructions
- **TODO_CONVERSION_SUMMARY.md** - Overview of the conversion process and metrics
- **ISSUE_ORGANIZATION_MAP.md** - Visual maps of priorities, dependencies, and implementation phases
- **GITHUB_ISSUES_README.md** - Detailed script documentation and troubleshooting
- **README_GITHUB_ISSUES.md** - Package overview and file guide

## Issue Templates

Issue templates are located in `.github/ISSUE_TEMPLATES_FROM_TODOS/` directory (12 templates total).

## Source Documents

Original TODO documents are preserved in `docs/development/`:
- `integration-todos.md` - Integration tasks and priorities
- `todos.md` - General TODO items

## Prerequisites

- GitHub CLI (`gh`) installed and authenticated
- Write access to the repository

## Methods

### Automated (Fastest)
Run the script to create all 12 issues at once:
```bash
./create-issues-from-todos.sh
```

### Manual (Full Control)
Copy content from individual templates in `.github/ISSUE_TEMPLATES_FROM_TODOS/` and paste into GitHub's issue creation form.

### Selective (Targeted)
Edit the script to comment out unwanted issues before running.

## Issues Created

The script creates 12 issues organized by priority:
- **High Priority (4 issues):** Core functionality
- **Medium Priority (5 issues):** Important enhancements  
- **Low Priority (3 issues):** Polish and optimizations

For detailed information, see the documentation files listed above.
