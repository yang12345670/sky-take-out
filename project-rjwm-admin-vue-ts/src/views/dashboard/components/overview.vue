<template>
  <div class="container">
    <h2 class="homeTitle">
      Today's data<i>{{ days[1] }}</i
      ><span><router-link to="statistics">Details</router-link></span>
    </h2>
    <div class="overviewBox">
      <ul>
        <li>
          <p class="tit">Turnover</p>
          <p class="num">$ {{ overviewData.turnover }}</p>
        </li>
        <li>
          <p class="tit">Valid Order Count</p>
          <p class="num">{{ overviewData.validOrderCount }}</p>
        </li>
        <li>
          <p class="tit">Order Completion Rate</p>
          <p class="num">
            {{ (overviewData.orderCompletionRate * 100).toFixed(0) }}%
          </p>
        </li>
        <li>
          <p class="tit">Average Order Value</p>
          <p class="num">$ {{ overviewData.unitPrice }}</p>
        </li>

        <li>
          <p class="tit">New Users</p>
          <p class="num">{{ overviewData.newUsers }}</p>
        </li>
      </ul>
    </div>
  </div>
</template>
<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import { getday } from '@/utils/formValidate'
@Component({
  name: 'Overview',
})
export default class extends Vue {
  @Prop() private overviewData!: any
  get days() {
    return getday()
  }
}
</script>
