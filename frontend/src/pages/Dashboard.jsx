import React, { useState, useEffect } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { Trash2, PlusCircle, IndianRupee } from 'lucide-react';

const Dashboard = ({ token }) => {
  const [debts, setDebts] = useState([]);
  const [schedule, setSchedule] = useState([]);
  const [summary, setSummary] = useState({ months: 0, interest: 0 });
  const [extraPayment, setExtraPayment] = useState(0);
  
  const [newDebt, setNewDebt] = useState({ name: '', balance: '', interestRate: '', minimumPayment: '' });

  const headers = {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };

  useEffect(() => {
    fetchDebts();
    fetchExtraPayment();
  }, []);

  const fetchDebts = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/debts', { headers });
      if (res.ok) {
        const data = await res.json();
        setDebts(data);
      }
    } catch (e) { console.error(e); }
  };

  const fetchExtraPayment = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/user/payment', { headers });
      if (res.ok) {
        const data = await res.json();
        setExtraPayment(data);
      }
    } catch (e) { console.error(e); }
  };

  const updateExtraPayment = async (val) => {
    setExtraPayment(val);
    try {
      await fetch('http://localhost:8080/api/user/payment', {
        method: 'PUT',
        headers,
        body: JSON.stringify({ extraMonthlyPayment: val })
      });
      // Will trigger useEffect
    } catch (e) { console.error(e); }
  };

  const calculateSnowball = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/calculator/snowball', { headers });
      if (res.ok) {
        const data = await res.json();
        setSummary({ months: data.totalMonthsToPayoff || 0, interest: data.totalInterestPaid || 0 });
        
        if (data.schedule && data.schedule.length > 0) {
          const chartData = data.schedule.map(month => {
            const totalBalance = month.debtsRemaining.reduce((sum, d) => sum + d.balance, 0);
            return { name: `M${month.month}`, balance: totalBalance };
          });
          
          const initialBalance = debts.reduce((sum, d) => sum + d.balance, 0);
          chartData.unshift({ name: 'M0', balance: initialBalance });
          
          setSchedule(chartData);
        } else {
          setSchedule([]);
        }
      }
    } catch (e) { console.error(e); }
  };

  useEffect(() => {
    calculateSnowball();
  }, [debts, extraPayment]);

  const handleAddDebt = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch('http://localhost:8080/api/debts', {
        method: 'POST',
        headers,
        body: JSON.stringify({
          name: newDebt.name,
          balance: parseFloat(newDebt.balance),
          interestRate: parseFloat(newDebt.interestRate),
          minimumPayment: parseFloat(newDebt.minimumPayment)
        })
      });
      if (res.ok) {
        setNewDebt({ name: '', balance: '', interestRate: '', minimumPayment: '' });
        fetchDebts();
      }
    } catch (e) { console.error(e); }
  };

  const handleDeleteDebt = async (id) => {
    try {
      const res = await fetch(`http://localhost:8080/api/debts/${id}`, { method: 'DELETE', headers });
      if (res.ok) {
        fetchDebts();
      }
    } catch (e) { console.error(e); }
  };

  const totalCurrentBalance = debts.reduce((sum, d) => sum + d.balance, 0);

  return (
    <div className="animate-fade-in" style={{ display: 'grid', gridTemplateColumns: '1fr 2.5fr', gap: '24px' }}>
      
      <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
        <div className="glass-panel">
          <h3 style={{ marginBottom: '16px', fontSize: '1.2rem' }}>Extra Monthly Payment</h3>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <IndianRupee size={20} color="var(--text-secondary)" />
            <input 
              type="number" 
              className="input-field" 
              value={extraPayment}
              onChange={(e) => updateExtraPayment(parseFloat(e.target.value) || 0)}
              style={{ flexGrow: 1 }}
            />
          </div>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginTop: '8px' }}>
            Amount to add on top of all minimum payments.
          </p>
        </div>

        <div className="glass-panel">
          <h3 style={{ marginBottom: '16px', fontSize: '1.2rem' }}>Add Debt</h3>
          <form onSubmit={handleAddDebt} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            <input placeholder="Name (e.g. Visa)" className="input-field" required value={newDebt.name} onChange={e => setNewDebt({...newDebt, name: e.target.value})} />
            <input type="number" step="0.01" placeholder="Balance (₹)" className="input-field" required value={newDebt.balance} onChange={e => setNewDebt({...newDebt, balance: e.target.value})} />
            <input type="number" step="0.01" placeholder="Interest Rate (%)" className="input-field" required value={newDebt.interestRate} onChange={e => setNewDebt({...newDebt, interestRate: e.target.value})} />
            <input type="number" step="0.01" placeholder="Minimum Payment (₹)" className="input-field" required value={newDebt.minimumPayment} onChange={e => setNewDebt({...newDebt, minimumPayment: e.target.value})} />
            <button type="submit" className="btn-primary" style={{ marginTop: '8px' }}><PlusCircle size={18} /> Add</button>
          </form>
        </div>

        <div className="glass-panel">
          <h3 style={{ marginBottom: '16px', fontSize: '1.2rem' }}>Your Debts</h3>
          {debts.length === 0 ? <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>No debts added yet.</p> : null}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxHeight: '300px', overflowY: 'auto', paddingRight: '4px' }}>
            {debts.map(d => (
              <div key={d.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px', background: 'rgba(255,255,255,0.05)', borderRadius: '8px' }}>
                <div>
                  <div style={{ fontWeight: '600', color: 'var(--text-primary)' }}>{d.name}</div>
                  <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>₹{d.balance.toFixed(2)} @ {d.interestRate}%</div>
                </div>
                <button onClick={() => handleDeleteDebt(d.id)} style={{ background: 'transparent', border: 'none', color: 'var(--accent-hover)', cursor: 'pointer', opacity: 0.8 }} title="Remove">
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '20px' }}>
          <div className="glass-panel" style={{ textAlign: 'center', padding: '20px' }}>
            <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '8px', textTransform: 'uppercase', letterSpacing: '1px' }}>Total Debt</div>
            <div className="gradient-text" style={{ fontSize: '2.5rem', fontWeight: 'bold' }}>₹{totalCurrentBalance.toFixed(2)}</div>
          </div>
          <div className="glass-panel" style={{ textAlign: 'center', padding: '20px' }}>
            <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '8px', textTransform: 'uppercase', letterSpacing: '1px' }}>Freedom In</div>
            <div className="gradient-text" style={{ fontSize: '2.5rem', fontWeight: 'bold' }}>{summary.months} <span style={{fontSize: '1rem', fontWeight: 'normal'}}>mos</span></div>
          </div>
          <div className="glass-panel" style={{ textAlign: 'center', padding: '20px' }}>
            <div style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', marginBottom: '8px', textTransform: 'uppercase', letterSpacing: '1px' }}>Total Interest</div>
            <div className="gradient-text" style={{ fontSize: '2.5rem', fontWeight: 'bold' }}>₹{summary.interest.toFixed(2)}</div>
          </div>
        </div>

        <div className="glass-panel" style={{ flexGrow: 1, minHeight: '450px', display: 'flex', flexDirection: 'column' }}>
          <h3 style={{ marginBottom: '20px', fontSize: '1.2rem' }}>Payoff Trajectory</h3>
          {schedule.length > 0 ? (
            <div style={{ flexGrow: 1, width: '100%' }}>
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={schedule} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
                  <defs>
                    <linearGradient id="colorBalance" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="var(--accent-color)" stopOpacity={0.5}/>
                      <stop offset="95%" stopColor="var(--accent-color)" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="var(--border-color)" vertical={false} />
                  <XAxis dataKey="name" stroke="var(--text-secondary)" tick={{ fill: 'var(--text-secondary)' }} axisLine={false} tickLine={false} dy={10} />
                  <YAxis stroke="var(--text-secondary)" tick={{ fill: 'var(--text-secondary)' }} axisLine={false} tickLine={false} tickFormatter={(val) => `₹${val}`} />
                  <Tooltip 
                    contentStyle={{ backgroundColor: 'var(--panel-bg)', backdropFilter: 'blur(10px)', border: '1px solid var(--border-color)', borderRadius: '8px', color: 'var(--text-primary)' }}
                    itemStyle={{ color: 'var(--accent-color)', fontWeight: 'bold' }}
                    labelStyle={{ color: 'var(--text-secondary)', marginBottom: '4px' }}
                    formatter={(value) => [`₹${value.toFixed(2)}`, 'Remaining Balance']}
                  />
                  <Area type="monotone" dataKey="balance" stroke="url(#colorBalance)" strokeWidth={3} fillOpacity={1} fill="url(#colorBalance)" activeDot={{ r: 6, fill: 'var(--accent-hover)' }} />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          ) : (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%', flexGrow: 1 }}>
              <div style={{ textAlign: 'center', color: 'var(--text-secondary)', padding: '40px', background: 'rgba(255,255,255,0.02)', borderRadius: '12px', border: '1px dashed var(--border-color)' }}>
                <IndianRupee size={48} style={{ opacity: 0.2, marginBottom: '16px' }} />
                <p>Add some debts to see your snowball projection!</p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
