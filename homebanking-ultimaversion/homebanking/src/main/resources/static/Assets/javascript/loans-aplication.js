const { createApp } = Vue;

createApp({
  data() {
    return {
        data: undefined,
        client: {},
        accounts: [],
        userLoans: [],
        loans: [],
        id:"",
        amount:"",
        account_destinated:"",
        payments:"",
         loans_id:"",
    };
  },
  created () {
    this.loadData()
    this.loan()
  },
  methods: {
    loadData() {
        axios.get("http://localhost:8080/api/clients/current")
        .then((response) => {
            this.data = response;
            console.log(this.data),
            this.client = response.data;
            this.accounts = response.data.accounts;
            this.userLoans = response.data.loans;
       })  
        .catch(err => console.log(err))
    },
    loan() {
        axios.get("http://localhost:8080/api/loans")
        .then((response) => {
            this.loans = response.data
        })
    }, 
    filterLoans:function(){
      return this.loans.filter(loan=>loan.id==this.id)[0]?.payments;
  },
    newTransaction() {
      axios.post('/api/loans', {amount:this.amount, payments:this.payments, id:this.loans_id, account_destinated:this.account_destinated})
      
      .then(response => {
         Swal.fire({
              title: 'Do you want to save the changes?',
              showDenyButton: true,
              showCancelButton: true,
              confirmButtonText: 'Save',
              denyButtonText: `Don't save`,
            }).then((result) => {
              /* Read more about isConfirmed, isDenied below */
              if (result.isConfirmed) {
                window.location.href = '/web/accounts.html';
              } else if (result.isDenied) {
                Swal.fire('Changes are not saved', '', 'info')
              }
            })
          })
      .catch(error => {
          console.log(error)
          this.error = error.response.data;
      });

  }, 

  logOut(){
      console.log("hola")
      axios.post('/api/logout')
      .then(response =>{
        window.location.href = '/web/index.html';
      })
    },

  transacAlert(){
      Swal.fire({
          title: 'Are you sure to make this transaction?',
          text: "Once sent, you could recover the amount",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#276221',
          cancelButtonColor: '#d33',
          confirmButtonText: 'Yes,I confirm!'
        }).then((result) => {
          if (result.isConfirmed) {
            this.newTransaction()
          }
        })
      
      },

      logoutAlert(){
        Swal.fire({
            title: 'Are you sure to logout?',
            text: "",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#276221',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, confirm it!'
          }).then((result) => {
            if (result.isConfirmed) {
              this.logOut()
            }
          })
        
        },  hamb() {
          let top = document.getElementById("hTop")
          top.classList.toggle("hTopOpen")
          let mid = document.getElementById("hMid")
          mid.classList.toggle("hMidOpen")
          let bot = document.getElementById("hBot")
          bot.classList.toggle("hBotOpen")
    }
  }
}).mount("#app");
