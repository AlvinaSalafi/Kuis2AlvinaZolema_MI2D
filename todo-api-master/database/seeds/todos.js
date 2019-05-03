
exports.seed = function(knex, Promise) {
  // Deletes ALL existing entries
  return knex('todos').del()
    .then(function () {
      // Inserts seed entries
      return knex('todos').insert([
        {user_id: 1, todo: 'coba', done: 1},
        {user_id: 2, todo: 'cobaa', done: 0}
        
      ]);
    });
};
