const { createApp } = Vue;

createApp({
  data() {
    return {
      data: undefined,
      accounts: [],
      clients: [],
      loans: [],
      cards: [],
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios
        .get("http://localhost:8080/api/clients/current")
        .then((response) => {
          this.data = response;
          this.accounts = response.data.accounts;
          this.clients = response.data;
          this.loans = response.data.loans;
          this.cards = response.data.cards
          console.log(this.cards)
        })
        .catch((err) => console.log(err));
    },
    parseDate(fecha) {
      let date = fecha.split("T")[0];
      let newDate = date.split("-").reverse().join("/");
      return newDate;
    },
    h: function(){
      document.querySelector('.htop').classList.toggle('openTop')
      document.querySelector('.hmid').classList.toggle('openMid')
      document.querySelector('.hbot').classList.toggle('openBot')
      document.querySelector('#modal').classList.toggle('modalOpen')
      
    }
  },
}).mount("#app");
