<template>
  <div id="demo">
    {{msg}}
  </div>
</template>

<script>
export default {
   
   
  name: 'HelloWorld',
  data () {
   
   
    return {
   
   
      msg: 'Hello Vue!'
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#demo{
   
   
  background-color: bisque;
  font-size: 20pt;
  color:darkcyan;
  margin-left: 30%;
  margin-right: 30%;
}
</style>