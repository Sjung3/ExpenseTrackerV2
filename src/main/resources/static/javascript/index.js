//TODO DOWNLOAD AXIOS LIBRARY... under code inspection...
//Header
let header = document.querySelector('.header');

//Welcome Container
let welcomeContainer = document.querySelector('.welcome-container');
let budgetList = document.querySelector('.budget-list');

//Budget Container
let budgetContainer = document.querySelector('.budget-container');
let budgetName = document.querySelector('.budget-name');
let budgetStartDate = document.querySelector('.start-date');
let budgetEndDate = document.querySelector('.end-date');
let budgetAmount = document.querySelector('.budget-amount');

//Budget Menu Container
let budgetMenuContainer = document.querySelector('.budget-menu-container');
let budgetInfoList = document.querySelectorAll('.budget-info-list');

//Expense Container
let expenseContainer = document.querySelector('.expense-container');
let expenseDate = document.querySelector('.expense-date');
let expenseCategory = document.querySelector('.expense-category');
let expenseAmount = document.querySelector('.expense-amount');
let expenseComment = document.querySelector('.expense-comment');

//Per Container
let perContainer = document.querySelector('.per-container');
let expenseList = document.querySelector('.expense-list');

//Are you sure container
let sureContainer = document.querySelector('.are-you-sure-container');

let budgetArray = [];
let expenseArray = [];
let budgetID = 0;

//Value set in getDates function and then used to ensure new expenses submitted is within the budget dates
let startDate = null;
let endDate = null;

let errorMessage = 'Something went wrong, please try again.';

window.addEventListener('load', async () => {
    const budgetData = await getBudgetData();
    addToBudgetArray(budgetData);
    createBudgetElements(budgetData);

    showWelcomePage();
    const todaysDate = await getTodaysDate();

    displayTodaysDate(todaysDate);
});

welcomeContainer.addEventListener('click', async (e) => {
    if (e.target.className === 'go-btn') {
        showBudgetMenuPage();

        //Sets the value of budgetID and dates which is used when adding a new expense.
        //Also identifies budget/expense to delete.
        getBudgetID(e);
        getBudgetDates(e);

        const expenseData = await getExpenseData(budgetID);
        addToExpenseArray(expenseData);
        const summary = await getTripSummery(budgetID);
        displayTripSummery(summary);
    }
});

welcomeContainer.addEventListener('click', (e) => {
    if (e.target.className === 'add-budget-btn') {
        showAddBudgetPage();
    }
});

welcomeContainer.addEventListener('click', async (e) => {
    if (e.target.className === 'delete-budget delete-btn') {
        const parent = getParent(e);
        const child = getChild(parent, 1);
        const id = getChildTextContent(child);

        document.querySelector('#budget-to-delete').textContent += id;
        showSureContainer();
        sureContainer.addEventListener('click', async (ev) => {

            if (ev.target.id === 'yes-btn') {
                const budgetToDelete = await deleteItem(e);
                if (budgetToDelete > 0) {
                    deleteElements(budgetList);
                    const budgetData = await getBudgetData();
                    addToBudgetArray(budgetData);
                    createBudgetElements(budgetData)
                    showWelcomePage();
                }
            } else if (ev.target.id === 'no-btn') {
                showWelcomePage()
            }
        });
    }
});

budgetContainer.addEventListener('click', async (e) => {
    if (e.target.className === 'submit-btn') {
        const budgetData = await saveBudget();
        addToBudgetArray(budgetData);
        clearInput(document.querySelectorAll('.budget-submitted'));
    }
});

budgetMenuContainer.addEventListener('click', (e) => {
    if (e.target.className === 'add-expense-btn') {
        showAddExpensePage();
    }
});

budgetMenuContainer.addEventListener('click', async (e) => {
    if (e.target.className === 'category-btn' || e.target.className === 'date-btn') {
        showPerPage();
        deleteElements(expenseList);

        const expenseData = await getExpenseData(budgetID);
        addToExpenseArray(expenseData);

        if (e.target.className === 'category-btn') {
            const spentPerCategory = await getSpentPerCategorySummery();
            createSpentSummeryElements(spentPerCategory)
        } else if (e.target.className === 'date-btn') {
            const spentPerDate = await getSpentPerDay();
            createSpentSummeryElements(spentPerDate)
        }
    }
});

expenseContainer.addEventListener('click', async (e) => {
    if (e.target.className === 'submit-btn') {
        const expenseData = await saveExpense();
        addToExpenseArray(expenseData);
        clearInput(document.querySelectorAll('.expense-submitted'));
    }
});
//Displays the details of spent per category when a user clicks on ie 'food'
perContainer.addEventListener('click', (e) => {
    if (e.target.className === 'spent-per') {
        const parent = getParent(e);
        const child = getChild(parent, 0);
        const showThese = getChildTextContent(child).split(' ');
        const innerListExpense = document.querySelectorAll('.inner-list-expense');

        for (const list of innerListExpense) {
            list.style.display = 'none';
            const fromExpenseList = getChildTextContent(list, 1);
            if (fromExpenseList.includes(showThese[0].toUpperCase())) {
                list.style.display = '';
            }
        }
    }
});

perContainer.addEventListener('click', async (e) => {
    if (e.target.className === 'delete-expense delete-btn') {
        const ctgOrNot = document.querySelector('.spent-per').textContent.split(' ');

        const dataToDelete = await deleteItem(e);
        console.log(dataToDelete + ' has been deleted')

        const expenseData = await getExpenseData(budgetID);
        addToExpenseArray(expenseData);
        deleteElements(expenseList);

        if (ctgOrNot[0].match(/^[A-Za-z]+$/)) {
            const spentPerCategory = await getSpentPerCategorySummery();
            createSpentSummeryElements(spentPerCategory)
        } else {
            const spentPerDate = await getSpentPerDay();
            createSpentSummeryElements(spentPerDate)
        }
    }
});

header.addEventListener('click', async (e) => {
    if (e.target.className === 'back-btn') {
        showBudgetMenuPage();
        deleteElements(budgetList);
        clearTextContent(budgetInfoList);

        const expenseData = await getExpenseData(budgetID);
        addToExpenseArray(expenseData);

        const summary = await getTripSummery(budgetID);
        displayTripSummery(summary);
    }
});
header.addEventListener('click', async (e) => {
    if (e.target.className === 'home-btn') {
        showWelcomePage();
        deleteElements(budgetList);
        clearTextContent(budgetInfoList);

        const budgetData = await getBudgetData();
        addToBudgetArray(budgetData);

        createBudgetElements(budgetData);
    }
});

//Retrieves all budget-items from the database and calls function to create budget elements.
//Used at load of welcome page
async function getBudgetData() {
    try {
        const response = await axios.get('api/getBudgets');
        return response.data;
    } catch (error) {
        alert(errorMessage);
    }
}

async function saveBudget() {
    try {
        startDate = new Date(budgetStartDate.value);
        endDate = new Date(budgetEndDate.value);
        let differenceInTime = endDate.getTime() - startDate.getTime();
        if (budgetName.value.length === 0 || budgetStartDate.value.length === 0 || budgetEndDate.value.length === 0 || budgetAmount.value === 0) {
            alert('Please fill in all fields.')
        } else {
            if (differenceInTime === 0 || differenceInTime > 1) {
                const response = await axios.post('api/saveBudget', {
                    'budgetName': budgetName.value,
                    'budgetStartDate': budgetStartDate.value,
                    'budgetEndDate': budgetEndDate.value,
                    'budgetAmount': budgetAmount.value
                });
                return response.data;
            } else {
                alert('End date can not be before start date.')
            }
        }
    } catch
        (error) {
        alert(errorMessage);
    }
}

function addToBudgetArray(budArray) {
    budgetArray = budArray;
}

async function getExpenseData(budgetID) {
    try {
        const response = await axios.post('/api/getExpenses', {'budgetID': budgetID,});
        return response.data;
    } catch (error) {
        alert(errorMessage);
    }
}

async function saveExpense() {
    try {
        if (expenseDate.value.length === 0 || expenseCategory.value.length === 0 || expenseAmount.value.length === 0) {
            alert('Only comment field can be empty')
        } else {
            if (expenseDate.value >= startDate && expenseDate.value <= endDate) {
                const response = await axios.post('api/saveExpense', {
                    'budgetID': budgetID,
                    'expenseDate': expenseDate.value,
                    'expenseCategory': expenseCategory.value,
                    'expenseAmount': expenseAmount.value,
                    'expenseComment': expenseComment.value
                });
                return response.data;
            } else {
                alert('Date is not within budget dates.')
            }
        }
    } catch
        (error) {
        alert(errorMessage)
    }
}

async function deleteItem(e) {
    try {
        const parent = getParent(e);
        const child = getChild(parent, 0);
        const id = getChildTextContent(child);
        const className = child.className;
        const ul = parent.closest("ul")

        if (className === 'expense-id') {
            const response = await axios.post('api/deleteExpense', {'expenseID': id});
            if (ul.className === "expense-list") {
                expenseList.removeChild(parent);
                return response.data;
            }
            return response.data;
        } else if (className === 'budget-id') {
            const response = await axios.post('api/deleteBudget', {'budgetID': id});
            return response.data;
        }
    } catch (error) {
        alert(errorMessage)
    }
}

function addToExpenseArray(expArray) {
    expenseArray = [];
    expenseArray = expArray;
}

async function getSpentPerDay() {
    try {
        const response = await axios.post('api/getSpentPerDay', {
            'budgetID': budgetID,
        });
        return response.data;
    } catch (error) {
        alert(errorMessage);
    }
}

async function getTodaysDate() {
    const response = await axios.get('/api/todaysDate');
    return response.data;
}

function displayTodaysDate(todaysDate) {
    document.querySelector('#todays-date').textContent += todaysDate;
}

async function getTripSummery(budgetID) {
    try {
        const response = await axios.post('/api/getSummery', {
            'budgetID': budgetID,
        });

        return response.data;

    } catch (e) {
    }
}

//TODO Can I use while or a for loop to populate textContent?
//Gets the summery from Java admin
function displayTripSummery(summary) {
    try {
        if (summary[6] === 'NaN') {
            summary[6] = .00;
        }
        document.querySelector('#budget-dates').textContent += summary[0] + ' - ' + summary[1];
        document.querySelector('#budget-name').textContent += summary[2];
        document.querySelector('#budget-total').textContent += 'Budget: €' + summary[3];
        document.querySelector('#total-day').textContent += 'Spent today: €' + summary[4];
        document.querySelector('#daily-budget').textContent += 'Daily budget: €' + summary[5];
        document.querySelector('#average-spent').textContent += 'Average spent: €' + summary[6];
        document.querySelector('#total-trip').textContent += 'Total for trip: €' + summary[7];
        document.querySelector('#remaining').textContent += 'Remaining: €' + summary[8];
        document.querySelector('#days-trip').textContent += 'Days on trip: ' + summary[9];
    } catch (e) {
    }
}

//Creates budget elements
//Used on welcome page
function createBudgetElements(budgets) {
    for (const budget of budgets) {

        const listElement = document.createElement('li');
        listElement.className = 'inner-list-budget';

        const id = document.createElement('p');
        id.className = 'budget-id';
        id.textContent = budget.budgetID;
        id.style.display = 'none';

        const budgetItem = document.createElement('p');
        budgetItem.className = 'budget';
        budgetItem.textContent = budget.budgetName + ' || ' + budget.budgetStartDate + ' - ' + budget.budgetEndDate +
            ' || €' + budget.budgetAmount;

        const goBtn = document.createElement('button');
        goBtn.className = 'go-btn';
        goBtn.type = 'submit';
        goBtn.textContent = 'Go';

        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-budget delete-btn';
        deleteButton.type = 'submit';
        deleteButton.innerText = 'Delete';

        listElement.appendChild(id);
        listElement.appendChild(budgetItem);
        listElement.appendChild(goBtn);
        listElement.appendChild(deleteButton);
        budgetList.appendChild(listElement);
    }
}

async function getSpentPerCategorySummery() {
    try {
        const response = await axios.post('/api/getCategorySummery',);
        return response.data;
    } catch (error) {
        alert(errorMessage);
    }
}

function createExpenseElements(compareWith) {
    for (const expense of expenseArray) {
        const listElement = document.createElement('li');
        listElement.className = 'inner-list-expense in-expense-list';

        const id = document.createElement('p');
        id.className = 'expense-id';
        id.textContent = expense.expenseID;
        id.style.display = 'none';

        const expenseItem = document.createElement('p');
        expenseItem.className = 'expense';
        if (expense.expenseComment === null) {
            expenseItem.textContent = expense.expenseDate + ' || ' + expense.expenseCategory + ' €' + expense.expenseAmount;
        } else {
            expenseItem.textContent = expense.expenseDate + ' || ' + expense.expenseCategory + ' €' + expense.expenseAmount +
                ' || ' + expense.expenseComment;
        }

        const deleteButton = document.createElement('button');
        deleteButton.className = 'delete-expense delete-btn';
        deleteButton.id = 'delete-expense';
        deleteButton.type = 'submit';
        deleteButton.textContent = 'Delete';

        listElement.appendChild(id);
        listElement.appendChild(expenseItem);
        listElement.appendChild(deleteButton);
        listElement.style.display = 'none';

        if (expense.expenseCategory === compareWith || expense.expenseDate === compareWith) {
            expenseList.appendChild(listElement);
        }
    }
}

function createSpentSummeryElements(spentPer) {
    for (const expense of spentPer) {
        const where = expense.split(' ');

        const listElement = document.createElement('li');
        listElement.className = 'inner-list-per in-expense-list';

        const spentPerDayItem = document.createElement('h2');
        spentPerDayItem.className = 'spent-per';
        spentPerDayItem.textContent = expense;

        listElement.appendChild(spentPerDayItem);
        expenseList.appendChild(listElement);
        createExpenseElements(where[0].toUpperCase());
    }
}

function getParent(e) {
    return e.target.parentElement;

}

function getChild(parent, index) {
    return parent.children[index];
}

function getChildTextContent(child) {
    return child.textContent;
}

//Specifies the variable budgetID to specify expense foreign key
//Used on click 'go-btn'
function getBudgetID(e) {
    const parent = getParent(e);
    const child = getChild(parent, 0);
    budgetID = getChildTextContent(child);
}


//Sets value of startDate and endDate variable that is then used to ensure the new expense added is within budget dates.
function getBudgetDates(e) {
    const parent = getParent(e);
    const child = getChild(parent, 1);
    const budgetTextContent = getChildTextContent(child);

    startDate = budgetTextContent.split(' ')[2];
    endDate = budgetTextContent.split(' ')[4];
}

//Delete all budget elements
//Used when returning to welcome page before rendering the updated budget items
function deleteElements(parent) {
    while (parent.firstChild) {
        parent.removeChild(parent.firstChild);
    }
}

function clearTextContent(list) {
    for (const text of list) {
        const textToDelete = text.textContent;
        text.textContent = textToDelete.replace(textToDelete, '');
    }
}

function clearInput(inputValue) {
    for (const input of inputValue) {
        input.value = '';
    }
}

function showWelcomePage() {
    welcomeContainer.style.display = '';
    budgetContainer.style.display = 'none';
    budgetMenuContainer.style.display = 'none';
    expenseContainer.style.display = 'none';
    perContainer.style.display = 'none';
    sureContainer.style.display = 'none';
}

function showAddBudgetPage() {
    welcomeContainer.style.display = 'none';
    budgetContainer.style.display = '';
    budgetMenuContainer.style.display = 'none';
    expenseContainer.style.display = 'none';
    perContainer.style.display = 'none';
    sureContainer.style.display = 'none';
}

function showBudgetMenuPage() {
    welcomeContainer.style.display = 'none';
    budgetContainer.style.display = 'none';
    budgetMenuContainer.style.display = '';
    expenseContainer.style.display = 'none';
    perContainer.style.display = 'none';
    sureContainer.style.display = 'none';
}

function showAddExpensePage() {
    welcomeContainer.style.display = 'none';
    budgetContainer.style.display = 'none';
    budgetMenuContainer.style.display = 'none';
    expenseContainer.style.display = '';
    perContainer.style.display = 'none';
    sureContainer.style.display = 'none';
}

function showPerPage() {
    welcomeContainer.style.display = 'none';
    budgetContainer.style.display = 'none';
    budgetMenuContainer.style.display = 'none';
    expenseContainer.style.display = 'none';
    perContainer.style.display = '';
    sureContainer.style.display = 'none';
}

function showSureContainer() {
    welcomeContainer.style.display = 'none';
    budgetContainer.style.display = 'none';
    budgetMenuContainer.style.display = 'none';
    expenseContainer.style.display = 'none';
    perContainer.style.display = 'none';
    sureContainer.style.display = '';
}