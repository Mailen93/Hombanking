const {createApp} = Vue 

 createApp({
    
    data() {
      return {
        currentCardBackground: Math.floor(Math.random()* 25 + 1), // just for fun :D
        cardName: "",
        cardNumber: "",
        cardMonth: "",
        cardYear: "",
        cardCvv: "",
        minCardYear: new Date().getFullYear(),
        amexCardMask: "#### ###### #####",
        otherCardMask: "#### #### #### ####",
        cardNumberTemp: "",
        isCardFlipped: false,
        focusElementStyle: null,
        isInputFocused: false,
        client: undefined,
        accounts: [],
        cards: [],
        loans: [],
        type:"",
        color:"",
        error:"",
      };
    },
    mounted() {
      this.cardNumberTemp = this.otherCardMask;
      document.getElementById("cardNumber").focus();
      this.loadData()
    },
    computed: {
      getCardType () {
        let number = this.cardNumber;
        let re = new RegExp("^4");
        if (number.match(re) != null) return "visa";
  
        re = new RegExp("^(34|37)");
        if (number.match(re) != null) return "amex";
  
        re = new RegExp("^5[1-5]");
        if (number.match(re) != null) return "mastercard";
  
        re = new RegExp("^6011");
        if (number.match(re) != null) return "discover";
  
        return "visa"; // default type
      },
          generateCardNumberMask () {
              return this.getCardType === "amex" ? this.amexCardMask : this.otherCardMask;
      },
      minCardMonth () {
        if (this.cardYear === this.minCardYear) return new Date().getMonth() + 1;
        return 1;
      }
    },
    watch: {
      cardYear () {
        if (this.cardMonth < this.minCardMonth) {
          this.cardMonth = "";
        }
      }
    },
    methods: {
      flipCard (status) {
        this.isCardFlipped = status;
      },
      focusInput (e) {
        this.isInputFocused = true;
        let targetRef = e.target.dataset.ref;
        let target = this.$refs[targetRef];
        this.focusElementStyle = {
          width: `${target.offsetWidth}px`,
          height: `${target.offsetHeight}px`,
          transform: `translateX(${target.offsetLeft}px) translateY(${target.offsetTop}px)`
        }
      },

      loadData(){
        axios.get(`http://localhost:8080/api/clients/current`)
        .then(response => {
            this.client = response.data
            this.accounts = this.client.account
            this.loans = this.client.loans
            this.cards = this.client.cards


        })
        .catch (error => error.mesagge)
    },

    Posnet(){
        axios.post('http://localhost:8080/api/pay', {number:this.number, cvv:this.cvv, amount:this.amount, description:this.description},{
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then (response => {
            Swal.fire({
                icon: 'success',
                title: 'Su pago fue exitoso!',
                showConfirmButton: false,
                timer: 1500
              
    })
        .then (response => {
            window.location.href = '/web/accounts.html'
    })})
        },

      blurInput() {
        let vm = this;
        setTimeout(() => {
          if (!vm.isInputFocused) {
            vm.focusElementStyle = null;
          }
        }, 300);
        vm.isInputFocused = false;
      }
    }
  })
  .mount("#app");