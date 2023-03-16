const { createApp } = Vue;

createApp({
  data() {
    return {
      data: undefined,
      client: null,
      transaction: [],
      accounts: null,
    };
  },
  created() {
    this.createTransaction();
    this.loadData()
  },
  methods: {
    createTransaction() {
      let url = new URLSearchParams(location.search).get("id");
      axios
        .get(`http://localhost:8080/api/accounts/${url}`)
        .then((response) => {
          this.transaction = response.data.transaction;
          console.log(this.transaction);
        })
        .catch((err) => console.log(err));
    },
    loadData() {
      axios
        .get("http://localhost:8080/api/clients/current")
        .then((response) => {
          this.data = response;
          this.client = response.data
        })
        .catch((err) => console.log(err));
    },
  },
}).mount("#app");
