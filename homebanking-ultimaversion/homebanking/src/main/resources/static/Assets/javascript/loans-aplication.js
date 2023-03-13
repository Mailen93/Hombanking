const { createApp } = Vue;

createApp({
  data() {
    return {
        data: undefined,
        client: {},
        accounts: [],
        userLoans: [],
        loans: []

    };
  },
  created () {
    this.loadData()
    this.getLoans()
  },
  methods: {
    loadData() {
        axios.get("http://localhost:8080/api/clients/current")
        .then((response) => {
            this.data = response;
            this.client = response.data;
            console.log(this.client)
            this.accounts = response.data.accounts;
            this.userLoans = response.data.loans;
        })
        .catch(err => console.log(err))
    },
    getLoans() {
        axios.get("http://localhost:8080/api/loans")
        .then((response) => {
            this.loans = response.data
            console.log(this.loans)
        })
        .catch(err => console.log(err))
    },
    hamb() {
        let top = document.getElementById("hTop")
        top.classList.toggle("hTopOpen")
        let mid = document.getElementById("hMid")
        mid.classList.toggle("hMidOpen")
        let bot = document.getElementById("hBot")
        bot.classList.toggle("hBotOpen")
    }
  }
}).mount("#app");
